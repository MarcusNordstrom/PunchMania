<?php 
session_start();
$host  = $_SERVER['HTTP_HOST'];
$uri   = rtrim(dirname($_SERVER['PHP_SELF']), '/\\');
$servername = "ddwap.mah.se:3306/ah7115";
$username = "ah7115";
$password = "Grupp1";
$dbname = "ah7115";
$conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password);
$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
switch ($_GET["type"]) {
	case 'login':
		$query = $conn->prepare("SELECT PW FROM user WHERE Uname=:uname");
    	$query->bindParam(':uname', $_POST["uname"]);
    	$query->execute();
    	$result = $query->fetch();
    	if (password_verify($_POST["pw"], $result["PW"])) {
    		$_SESSION["uname"] = $_POST["uname"];
    		$conn = null;
    		$extra = 'index.php';
    		header("Location: https://$host$uri/$extra");
    		die();
    	} else {
    		$conn = null;
    		$extra = 'login.php?error=1';
  			header("Location: https://$host$uri/$extra");
  			die();
    	}
		break;
	case 'reg':
		$hashpw = password_hash($_POST["pw"], PASSWORD_DEFAULT);
		$query = $conn->prepare("SELECT Uname FROM user WHERE Uname=:uname");			//check if user exists
    	$query->bindParam(':uname', $_POST["uname"]);
    	$query->execute();
    	$checkuname = $query->fetch();
    	if (!empty($checkuname["uname"])) {
    		$conn = null;
  			$extra = 'login.php?reg=true&error=1';
  			header("Location: https://$host$uri/$extra");
  			die();
    	}
    	$ins = $conn->prepare("INSERT INTO user (Uname, PW) VALUES (:uname, :hash)");
    	$ins->bindParam(':uname', $_POST["uname"]);
    	$ins->bindParam(':hash', $hashpw);
    	$ins->execute();
    	$_SESSION["uname"] = $_POST["uname"];
    	$conn = null;
    	$extra = 'index.php';
    	header("Location: https://$host$uri/$extra");
    	die();
		break;
	case 'queue':
		$query = $conn->prepare("SELECT count(*) as num FROM queue WHERE ID < ( SELECT ID FROM queue WHERE Name = :name )+1");
		$query->bindParam(':name', $_SESSION["uname"]);
		$query->execute();
		$query = $query->fetch();
		if($query["num"] == 0) {
			$ins = $conn->prepare("INSERT INTO queue (Name) VALUES (:name)");
			$ins->bindParam(':name', $_SESSION["uname"]);
			$ins->execute();
			$conn = null;
			$extra = 'index.php?queue=true';
    		header("Location: https://$host$uri/$extra");
    		die();
		} else {
			$del = $conn->prepare("DELETE FROM `queue` WHERE `Name` = :name");
			$del->bindParam(':name', $_SESSION["uname"]);
			$del->execute();
			$conn = null;
			$extra = 'index.php?queue=false';
    		header("Location: https://$host$uri/$extra");
    		die();
		}
		break;
	default:
		$extra = 'index.php';
    	header("Location: https://$host$uri/$extra");
    	die();
		break;
}

 ?>