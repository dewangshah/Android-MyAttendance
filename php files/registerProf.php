<?php
require "init.php";
$id=1;
$name="Atul Kachare";
$user_name="atul_kachare";
$user_pass="ak123";
$img ="";

$sql_query="insert into prof values('$id','$name','$user_name','$user_pass','$img');";

if(mysqli_query($con,$sql_query))
{
   echo "<h3>Data Insertion Success..</h3>";
}
else
{
   echo "Data Insertion Error..".mysqli_error($con);
}



?>