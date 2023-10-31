package com.example.demo.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import javax.servlet.ServletRequest
import javax.servlet.http.HttpServletRequest

import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost

@RestController
class HelloWorldController() {

  @GetMapping("/test1/{id}")
  fun test1(@PathVariable name: String): String {
    val host = BasicJvmScriptingHost()
    // ruleid: scripting-host-eval
    val result = host.eval("""
      "Hello " + "${name}"
    """)
    return result
  }

  @PostMapping("/test2")
  fun test2(@RequestBody search: String): List<Message> {
    val host = BasicJvmScriptingHost()
    val script = """
      val a = 2
      val b = 3
      fun add(x: Int, y: Int): Int = x + y
      add(a, ${search})
    """
    // ruleid: scripting-host-eval
    val result = host.compileAndEval(script)
    return result
  }

  @DeleteMapping("/{id}")
  fun test3(@PathVariable id: String): ResponseEntity<Void> {
    val search = id + "foobar"
    val script = """
      val a = 2
      val b = 3
      fun add(x: Int, y: Int): Int = x + y
      add(a, ${search})
    """
    // ruleid: scripting-host-eval
    val result = BasicJvmScriptingHost().compileAndEval(script)
    return result
  }

  @GetMapping("/test1/{id}")
  fun okTest1(@PathVariable name: Int): String {
    val host = BasicJvmScriptingHost()
    // ok: scripting-host-eval
    val result = host.eval("""
      "Hello " + "${name}"
    """)
    return result
  }

  @PostMapping("/test2")
  fun okTest2(@RequestBody search: String): List<Message> {
    val host = BasicJvmScriptingHost()
    val script = """
      val a = 2
      val search = 3
      fun add(x: Int, y: Int): Int = x + y
      add(a, search)
    """
    // ok: scripting-host-eval
    val result = host.compileAndEval(script)
    return result
  }

  @DeleteMapping("/{id}")
  fun okTest3(@PathVariable id: String): ResponseEntity<Void> {
    val search = id.toInt() + "foobar"
    val script = """
      val a = 2
      val b = 3
      fun add(x: Int, y: Int): Int = x + y
      add(a, ${search})
    """
    // ok: scripting-host-eval
    val result = BasicJvmScriptingHost().compileAndEval(script)
    return result
  }

  @DeleteMapping("/{id}")
  fun okTest4(@PathVariable id: String): ResponseEntity<Void> {
    val search = (id != null) + "foobar"
    val script = """
      val a = 2
      val b = 3
      fun add(x: Int, y: Int): Int = x + y
      add(a, ${search})
    """
    // ok: scripting-host-eval
    val result = BasicJvmScriptingHost().compileAndEval(script)
    return result
  }
}
