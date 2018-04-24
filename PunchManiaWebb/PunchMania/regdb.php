<?php
session_start();
$uname = $_POST["uname"];
$pw = $_POST["pw"];
if (empty($uname) or empty($pw)) {
  $host  = $_SERVER['HTTP_HOST'];
  $uri   = rtrim(dirname($_SERVER['PHP_SELF']), '/\\');
  $extra = 'login.php?reg=true';
  header("Location: http://$host$uri/$extra");
  die();
};
$hashpw = password_hash($pw, PASSWORD_DEFAULT);
$servername = "ddwap.mah.se:3306/ah7115";
$username = "ah7115";
$password = "Grupp1";
$dbname = "ah7115";
$conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password);
    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    $query = $conn->prepare("SELECT uname FROM user WHERE uname=:uname");			//check if user exists
    $query->bindParam(':uname', $uname);
    $query->execute();
    $checkuname = $query->fetch();
    if (!empty($checkuname["uname"])) {
    	$host  = $_SERVER['HTTP_HOST'];
  		$uri   = rtrim(dirname($_SERVER['PHP_SELF']), '/\\');
  		$extra = 'login.php?reg=true';
  		header("Location: https://$host$uri/$extra");
  		die();
    }
    $ins = $conn->prepare("INSERT INTO user (Uname, PW) VALUES (:uname, :hash)");
    $ins->bindParam(':uname', $uname);
    $ins->bindParam(':hash', $hashpw);
    $ins->execute();
	$conn = null;
	$_SESSION["uname"] = $uname;
	$host  = $_SERVER['HTTP_HOST'];
	$uri   = rtrim(dirname($_SERVER['PHP_SELF']), '/\\');
	$extra = 'index.php';
	header("Location: https://$host$uri/$extra");
	die();
?>