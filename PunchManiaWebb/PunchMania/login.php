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
	if (isset($_GET["error"])) {
		echo "Någon har det användarnamnet!";
	}
	echo "Registrera dig";
	echo '<form action="dbmanager.php?type=reg" method="POST">
	<input type="text" name="uname" pattern=".{3,15}" required>
	<input type="password" name="pw" required>
	<input type="submit">
</form>';
} else {
	if (isset($_GET['error'])) {
		echo "Fel lösenord!";
	}
	echo "Logga in";
	echo '<form action="dbmanager.php?type=login" method="POST">
	<input type="text" name="uname" pattern=".{3,15}" required>
	<input type="password" name="pw" required>
	<input type="submit">
</form>';
}
?>
</body>
</html>