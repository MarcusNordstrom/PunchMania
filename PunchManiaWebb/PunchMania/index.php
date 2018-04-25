<?php session_start();
function countQ() {
	$servername = "ddwap.mah.se:3306/ah7115";
	$username = "ah7115";
	$password = "Grupp1";
	$dbname = "ah7115";
	$conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password);
	$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	$query = $conn->prepare("SELECT count(*) as num FROM queue WHERE ID < ( SELECT ID FROM queue WHERE Name = :name )+1");
	$query->bindParam(':name', $_SESSION["uname"]);
	$query->execute();
	$query = $query->fetch();
	$conn = null;
	return $query["num"];
}
function getHSList($name) {
	$servername = "ddwap.mah.se:3306/ah7115";
	$username = "ah7115";
	$password = "Grupp1";
	$dbname = "ah7115";
	$conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password);
	$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	if ($name == null) {
		$query = $conn->prepare("SELECT * FROM hslist ORDER BY Score DESC LIMIT 10");
		$query->execute();
		$query = $query->fetchAll();
		$place = 1;
		echo "<br>";
		foreach ($query as $row) {
			echo "<p>" . $place . ". " . $row['Name'] . " - " . $row['Score']. "</p>";
			$place++;
		}
	} else {
		$query = $conn->prepare("SELECT * FROM hslist WHERE Name=:name ORDER BY Score DESC LIMIT 100");
		$query->bindParam(':name', $_GET["hsUser"]);
		$query->execute();
		$query = $query->fetchAll();
		$place = 1;
		echo "<br>";
		foreach ($query as $row) {
			echo "<p>" . $place . ". " . $row['Name'] . " - " . $row['Score']. "</p>";
			$place++;
		}
	}
}
 ?>
<!DOCTYPE html>
<html>
<head>
	<title>PunchMania</title>
	<meta charset="utf-8">
	<!-- <meta http-equiv="refresh" content="5"> -->
</head>
<body>
<?php  
	if (isset($_SESSION["uname"])) {
		echo "Välkommen " . $_SESSION["uname"]. "<br>";
		$queueNr = countQ();
		if ($queueNr > 0) {
			echo "Du har plats " . $queueNr . "<br>";
			echo '<button><a href="dbmanager.php?type=queue">Ta bort mig från kön</a></button><br>';
		} else {
			echo '<button><a href="dbmanager.php?type=queue">Ställ mig i kö!</a></button><br>';
		}
		echo '<button><a href="index.php?logout=true">Logga ut</a></button>';	
	} else {
		echo '<button><a href="login.php">Logga in</a></button><br>
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
	if(!isset($_GET["hsUser"])) {
		getHSList(null);
	} else {
		getHSList($_GET["hsUser"]);
	}
	
?>
</body>
<p>Sök Highscore:</p>
<form action="index.php" method="GET">
	<input type="text" name="hsUser">
</form>
</html>