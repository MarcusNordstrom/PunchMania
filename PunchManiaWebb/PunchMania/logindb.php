<?php session_start();
$pwtest = false;
$uname = $_POST["uname"];
$pw = $_POST["pw"];
$host  = $_SERVER['HTTP_HOST'];
$uri   = rtrim(dirname($_SERVER['PHP_SELF']), '/\\');
if (empty($uname) or empty($pw)) {
  $extra = 'login.php';
  header("Location: http://$host$uri/$extra");
  die();
};
$servername = "ddwap.mah.se:3306/ah7115";
$username = "ah7115";
$password = "Grupp1";
$dbname = "ah7115";
$conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password);
    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    $ins = $conn->prepare("SELECT PW FROM user WHERE Uname=:uname");
    $ins->bindParam(':uname', $uname);
    $ins->execute();
    $result = $ins->fetch();
$conn = null;
$hash = $result["PW"];
$pwtest = password_verify($pw, $hash);
if($pwtest == true ) {
    $_SESSION["uname"] = $uname;
    $extra = 'index.php';
    header("Location: http://$host$uri/$extra");
    die();
}else {
  $extra = 'login.php';
  header("Location: http://$host$uri/$extra");
  die();
}
 ?>