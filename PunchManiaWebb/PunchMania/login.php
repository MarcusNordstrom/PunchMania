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
if (isset($_GET["reg"])) {
	echo "Registrera dig";
	echo '<form action="regdb.php" method="POST">
	<input type="text" name="uname" pattern=".{3,15}">
	<input type="password" name="pw">
	<input type="submit">
</form>';
} else {
	echo "Logga in";
	echo '<form action="logindb.php" method="POST">
	<input type="text" name="uname" pattern=".{3,15}">
	<input type="password" name="pw">
	<input type="submit">
</form>';
}
?>
</body>
</html>