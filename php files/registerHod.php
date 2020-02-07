<?php
require "init.php";
$id=2;
$name="Uma Rao";
$user_name="uma_rao";
$user_pass="ur123";
$img ="";

$sql_query="insert into hod values('$id','$name','$user_name','$user_pass','$img');";

if(mysqli_query($con,$sql_query))
{
   echo "<h3>Data Insertion Success..</h3>";
}
else
{
   echo "Data Insertion Error..".mysqli_error($con);
}



?>