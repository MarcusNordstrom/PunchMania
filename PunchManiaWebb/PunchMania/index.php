<?php session_start() ?>
<!DOCTYPE html>
<html>
<head>
	<title>PunchMania</title>
	<meta charset="utf-8">
	<style type="text/css"></style>
</head>
<body>
<?php  
	if (isset($_SESSION["uname"])) {
		echo "Välkommen " . $_SESSION["uname"];
		echo '<button><a href="index.php?logout=true">Logga ut</a></button>';
	} else {
		echo '<button><a href="login.php">Logga in</a></button>
<button><a href="login.php?reg=true">Registrera dig</a></button>';
	}
	if (isset($_GET["logout"])) {
		session_destroy();
		$host  = $_SERVER['HTTP_HOST'];
		$uri   = rtrim(dirname($_SERVER['PHP_SELF']), '/\\');
		$extra = 'index.php';
		header("Location: https://$host$uri/$extra");
		die();
	}
?>
</body>
</html>