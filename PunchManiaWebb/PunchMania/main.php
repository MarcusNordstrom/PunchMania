<?php
header('Access-Control-Allow-Origin: *');
session_start();
$servername = "ddwap.mah.se:3306/ah7115";
$username = "ah7115";
$password = "Grupp1";
$dbname = "ah7115";
$GLOBALS["conn"] = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password);
$GLOBALS["conn"]->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
function index($site) {
	switch ($site) {
		case null:
			templateBody(null, null, null);
			break;
		case "login":
			templateBody("login", "0", "0");
			break;
		case "logout":
			templateBody("logout", "0", "0");
			break;
		case "line":
			templateBody("line", null, null);
			break;
		case "register":
			templateBody("register", "0", "0");
			break;
		case "user":
			if (isset($_GET["user"])) {
				templateBody("user", $_GET["user"], "0");
			}
		default:
			# code...
			break;
	}
}

function templateBody($info, $highscore, $queue) {
	echo '<div class="container-fluid">
      <div class="row header">
        <div class="col-lg">
          <h2 class="title"><a href="index.php">PunchMania</a></h2>
        </div>
      </div>
      <div class="row info">
        <div class="col-lg info">
        	<h2 class="title"><a href="index.php">PunchMania</a></h2>';
        getInfo($info);
        echo '</div>
      </div>
      <div class="row list">';
        getHSList($highscore);
        getQueue($queue);
        echo '</div>
    </div>';
}
function getHSList($name) {
	if ($name == null) {
		echo '<div class="col-lg hs">';
		echo '<div id="hs"></div>
          <script type="text/javascript">
            setInterval(function(){
              var xmlhttp = new XMLHttpRequest();
              xmlhttp.onreadystatechange = function() {
              	if (this.readyState == 4 && this.status == 200) {
              		var tbodyPosition = $(".hs tbody").scrollTop();
                	document.getElementById("hs").innerHTML = this.responseText;
    				$(".hs tbody").scrollTop(tbodyPosition);
              	}
              };
              xmlhttp.open("GET", "https://ddwap.mah.se/ah7115/PunchMania/main.php?js=hs", true);
              xmlhttp.send();
            }, 500);
          </script>';
	} elseif ($name == "0") {
		echo '';
	} else {
		echo '<div id="hs"></div>
          <script type="text/javascript">
            setInterval(function(){
              var xmlhttp = new XMLHttpRequest();
              xmlhttp.onreadystatechange = function() {
              if (this.readyState == 4 && this.status == 200) {
                var tbodyPosition = $(".hs tbody").scrollTop();
               	document.getElementById("hs").innerHTML = this.responseText;
    			$(".hs tbody").scrollTop(tbodyPosition);
              }
              };
              xmlhttp.open("GET", "https://ddwap.mah.se/ah7115/PunchMania/main.php?js=hs&user='.$name.'", true);
              xmlhttp.send();
            }, 500);
          </script>';
	}
	echo '</div>';
}
function getInfo($info){
	switch ($info) {
		case null:
			echo '<div id="info"></div>
          	<script type="text/javascript">
            setInterval(function(){
              var xmlhttp = new XMLHttpRequest();
              xmlhttp.onreadystatechange = function() {
              if (this.readyState == 4 && this.status == 200) {
              	var previousState = document.getElementById("info").innerHTML;
                var state = this.responseText;
                if(previousState != state) {
                	document.getElementById("info").innerHTML = state;
                }
              }
              };
              xmlhttp.open("GET", "https://ddwap.mah.se/ah7115/PunchMania/main.php?js=info", true);
              xmlhttp.send();
            }, 1000);
          </script>';
			break;
		case "login":
			echo '<form action="index.php?site=login" method="POST">
            <label>Username:</label><br>
          <input type="text" name="uname" pattern=".{3,15}" required><br>
            <label>Password:</label><br>
          <input type="password" name="pw" required><br><br>
          <input type="submit" value="Logga in" class="btn">
        </form>';
        	if (isset($_POST["uname"]) && isset($_POST["pw"])) {
        		$query = $GLOBALS["conn"]->prepare("SELECT * FROM user WHERE Uname=:uname");
    			$query->bindParam(':uname', $_POST["uname"]);
    			$query->execute();
    			$result = $query->fetch();
    			if (password_verify($_POST["pw"], $result["PW"])) {
    				$_SESSION["uname"] = $result["Uname"];
    				redirect("index.php");
    			} else {
    				redirect("index.php?site=login&error=1");		
    			}
        	}
			break;
		case "logout":
			session_destroy();
			redirect("index.php");
			break;
		case "line":
			$query = $GLOBALS["conn"]->prepare("SELECT count(*) as num FROM queue WHERE ID < ( SELECT ID FROM queue WHERE Name = :name )+1");
			$query->bindParam(':name', $_SESSION["uname"]);
			$query->execute();
			$query = $query->fetch();
			if($query["num"] == 0) {
				$ins = $GLOBALS["conn"]->prepare("INSERT INTO queue (Name) VALUES (:name)");
				$ins->bindParam(':name', $_SESSION["uname"]);
				$ins->execute();
				redirect("index.php");
    			die();
			} else {
				$del = $GLOBALS["conn"]->prepare("DELETE FROM `queue` WHERE `Name` = :name");
				$del->bindParam(':name', $_SESSION["uname"]);
				$del->execute();
    			redirect("index.php");
			}
			break;
		case "register":
			if (isset($_GET["error"])) {
				echo "<p>Användarnamnet finns!</p>";
			}
			echo '<form action="index.php?site=register" method="POST">
				<label>Username:</label><br>
          		<input type="text" name="uname" pattern=".{3,15}" required><br>
            	<label>Password:</label><br>
          		<input type="password" name="pw" required><br><br>
          		<input type="submit" value="Registrera dig" class="btn"></form>';
			if (isset($_POST["uname"]) && isset($_POST["pw"])) {
				$hashpw = password_hash($_POST["pw"], PASSWORD_DEFAULT);
				$query = $GLOBALS["conn"]->prepare("SELECT Uname FROM user WHERE Uname=:uname");			//check if user exists
    			$query->bindParam(':uname', $_POST["uname"]);
    			$query->execute();
    			$checkuname = $query->fetch();
    			if (!empty($checkuname["Uname"])) {
		    		redirect("index.php?site=register&error=1");
		    	}
   			 	$ins = $GLOBALS["conn"]->prepare("INSERT INTO user (Uname, PW) VALUES (:uname, :hash)");
   			 	$ins->bindParam(':uname', $_POST["uname"]);
	    		$ins->bindParam(':hash', $hashpw);
    			$ins->execute();
    			$_SESSION["uname"] = $_POST["uname"];
    			redirect("index.php");
				}
			break;
		case "user":
			echo '<a href="index.php?site=user&user='.$_GET["user"].'"><h2 class="name">'.$_GET["user"].'</h2></a>';
			getUserStats($_GET["user"]);
			break;
		default:
			# code...
			break;
	}

}
function getQplace() {
	if (isset($_SESSION["uname"])) {
		$query = $GLOBALS["conn"]->prepare("SELECT count(*) as num FROM queue WHERE ID < ( SELECT ID FROM queue WHERE Name = :name )+1");
		$query->bindParam(':name', $_SESSION["uname"]);
		$query->execute();
		$query = $query->fetch();
		if ($query["num"] > 0) {
			echo '<button class="btn" onclick="';
			echo "var xmlhttp = new XMLHttpRequest();xmlhttp.open('GET', 'https://ddwap.mah.se/ah7115/PunchMania/main.php?js=line', true);xmlhttp.send();";
			echo '">Ta bort mig <br>från köplats '. $query["num"] .'</button>';
		} else {
			echo '<button class="btn" onclick="';
			echo "var xmlhttp = new XMLHttpRequest();xmlhttp.open('GET', 'https://ddwap.mah.se/ah7115/PunchMania/main.php?js=line', true);xmlhttp.send();";
			echo '">Ställ mig i kö!</button>';
		}
	}
}
function getUserStats($user) {
	$queryq = $GLOBALS["conn"]->prepare("SELECT count(*) as num FROM queue WHERE ID < ( SELECT ID FROM queue WHERE Name = :name )+1");
	$queryq->bindParam(':name', $user);
	$queryq->execute();
	$queryq = $queryq->fetch();
	if ($queryq["num"] > 0) {
		echo "<p>Queue: ". $queryq["num"]."</p>";
	} else {
		echo "<p>Queue: Not in queue</p>";
	}
	$queryhs = $GLOBALS["conn"]->prepare("SELECT * FROM `hslist` WHERE `Name` = :name AND `Score` = (SELECT MAX(Score) FROM `hslist` WHERE Name = :name1)");
	$queryhs->bindParam(':name', $user);
	$queryhs->bindParam(':name1', $user);
	$queryhs->execute();
	$queryhs = $queryhs->fetch();
	if(isset($queryhs["Score"])) {
		echo '<p>Best score: '. $queryhs["Score"] .'</p>';
	} else {
		echo "<p>Best score: No score</p>";
	}
}
function getQueue($queue) {
	if ($queue == null) {
		echo '<div class="col-lg q">';
		echo '<div id="q"></div>
          <script type="text/javascript">
            setInterval(function(){
              var xmlhttp = new XMLHttpRequest();
              xmlhttp.onreadystatechange = function() {
              if (this.readyState == 4 && this.status == 200) {
                document.getElementById("q").innerHTML = this.responseText;
              }
              };
              xmlhttp.open("GET", "https://ddwap.mah.se/ah7115/PunchMania/main.php?js=q", true);
              xmlhttp.send();
            }, 500);
          </script>';
		echo '</div>';
	}
}
function tableStart() {
	echo "<table><tbody>";
}

function tableEnd() {
	echo '</tbody></table>';
}

function redirect($extra) {
	$host  = $_SERVER['HTTP_HOST'];
	$uri   = rtrim(dirname($_SERVER['PHP_SELF']), '/\\');
	header("Location: https://$host$uri/$extra");
}
if (isset($_GET["js"])) {
	switch ($_GET["js"]) {
		case "hs":
			if (isset($_GET["user"])) {
				$query = $GLOBALS["conn"]->prepare("SELECT * FROM hslist WHERE Name LIKE :name ORDER BY Score DESC LIMIT 100");
				$name = $_GET["user"] . "%";
				$query->bindParam(':name',  $name);
				$query->execute();
				$query = $query->fetchAll();
				$place = 1;
				if (!empty($query)) {
					echo '<h2>Highscore</h2>';
					tableStart();
					foreach ($query as $row) {
					echo '<tr><td><a href="index.php?site=user&user='.$row["Name"].'">'.$place.'</a></td><td><a href="index.php?site=user&user='.$row["Name"].'">'.$row["Name"].'</a></td><td><a href="index.php?site=user&user='.$row["Name"].'">'.$row["Score"].'</a></td></tr>';
					$place++;
					}
				}
				tableEnd();
			} else {
				$query = $GLOBALS["conn"]->prepare("SELECT * FROM hslist ORDER BY Score DESC LIMIT 100");
				$query->execute();
				$query = $query->fetchAll();
				$place = 1;
				echo '<h2>Highscore</h2>';
				tableStart();
				foreach ($query as $row) {
					echo '<tr><td><a href="index.php?site=user&user='.$row["Name"].'">'.$place.'</a></td><td><a href="index.php?site=user&user='.$row["Name"].'">'.$row["Name"].'</a></td><td><a href="index.php?site=user&user='.$row["Name"].'">'.$row["Score"].'</a></td></tr>';
					$place++;
				}
				tableEnd();
			}
			break;
		case "q":
			$query = $GLOBALS["conn"]->prepare("SELECT * FROM queue ORDER BY ID ASC LIMIT 100");
			$query->execute();
			$query = $query->fetchAll();
			$place = 1;
			echo ' <h2>Queue</h2>';
			tableStart();
			foreach ($query as $row) {
				echo '<tr><td><a href="index.php?site=user&user='.$row["Name"].'">'.$place.'</a></td><td><a href="index.php?site=user&user='.$row["Name"].'">'.$row["Name"].'</a></td></tr>';
				$place++;
			}
			tableEnd();
			break;
		case "info":
			if (isset($_SESSION["uname"])) {
				echo '<h3>Välkommen '.$_SESSION["uname"].'</h3>';
				getQplace();
				echo '<a href="index.php?site=logout"><button class="btn">Logga ut</button></a>';
			} else {
				echo '<a href="index.php?site=login"><button class="btn">Logga in</button></a>
          		<a href="index.php?site=register"><button class="btn">Registrera dig</button></a>';
			}
		break;
		case 'line':
			$query = $GLOBALS["conn"]->prepare("SELECT count(*) as num FROM queue WHERE ID < ( SELECT ID FROM queue WHERE Name = :name )+1");
			$query->bindParam(':name', $_SESSION["uname"]);
			$query->execute();
			$query = $query->fetch();
			if($query["num"] == 0) {
				$ins = $GLOBALS["conn"]->prepare("INSERT INTO queue (Name) VALUES (:name)");
				$ins->bindParam(':name', $_SESSION["uname"]);
				$ins->execute();
			} else {
				$del = $GLOBALS["conn"]->prepare("DELETE FROM `queue` WHERE `Name` = :name");
				$del->bindParam(':name', $_SESSION["uname"]);
				$del->execute();
			}
			break;
		default:
			# code...
			break;
	}
}
?>