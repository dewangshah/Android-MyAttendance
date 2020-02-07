<?php
require "init.php";
$id=37;
$name="Dewang Shah";
$user_name="M2013CM1063";
$user_pass="10767";
$img ="";

$sql_query="insert into stud values('$id','$name','$user_name','$user_pass','$img');";

if(mysqli_query($con,$sql_query))
{
   echo "<h3>Data Insertion Success..</h3>";
}
else
{
   echo "Data Insertion Error..".mysqli_error($con);
}



?>