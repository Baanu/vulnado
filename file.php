<?php

// Controller

namespace App\Http\Controllers;
 
use App\Http\Controllers\Controller;
use App\Models\Post;
use Illuminate\Support\Facades\DB;
 
class UserController extends Controller
{
  public function test0($col)
  {
    $input = 'value '.$col;
    // ruleid: laravel-code-injection
    if (assert("$x == $input")) {
      $data = "smth";
    } else {
      $data = "foobar";
    }

    return view('user.index', ['data' => $data]);
  }

  public function okTest1($message, $user)
  {
    $input = 'value';
    // ok: laravel-code-injection
    if (assert($x == $input)) {
      $data = "smth";
    } else {
      $data = "foobar";
    }
    return view('user.index', ['data' => $data]);
  }

}

class ProvisionServer extends Controller
{
  public function __invoke($message)
  {
    // ruleid: laravel-code-injection
    $func = create_function('$a,$b', 'return "message '.$message.' " . log($a * $b);');
    $output = $func();
    return view('user.index', ['output', $output]);
  }
}

class OkProvisionServer extends Controller
{
  public function __invoke($name)
  {
    // ok: laravel-code-injection
    $func = create_function('$a,$b', 'return "message " . log($a * $b);');
    $output = $func();
    return view('user.index', ['output', $output]);
  }
}

// Router

use Illuminate\Support\Facades\Route;

Route::get('/posts/{type}/{page}', function ($type, $page) {
  $rep = 'name-'.$type;
  $str = '';
  // ruleid: laravel-code-injection
  $output = eval("\$str = \"$rep\";");
  return view('user.index', ['result', $output]);
});

Route::get('/posts-ok/{post}', function ($name) {
  $type = getValueFromSafePlace();
  $rep = 'name-'.$type;
  // ok: laravel-code-injection
  $output = eval("\$str = \"$rep\";");
  return view('user.index', ['result', $output]);
});

// Middleware

use Illuminate\Http\Request;

class AfterMiddleware
{
  public function handle($request, Closure $next)
  {
    $response = $next($request);
    $data = $request->input('col');
    // ruleid: laravel-code-injection
    $func = create_function('$a,$b', 'return "message '.$data.' " . log($a * $b);');
    $output = $func();

    log($output);
    return $response;
  }
}
