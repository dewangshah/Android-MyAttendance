<?php
require "init.php";
$user_name=$_POST["login_name"];

$sql_query="select agg from studsub where stud_id like '$user_name' and passwd like '$user_pass';";

$result=mysqli_query($con,$sql_query);

if(mysqli_num_rows($result)>0)
{
   $row=mysqli_fetch_assoc($result);
   $name=$row["name"];
   echo "true ".$name;
}
else
{
   echo "Login Failed, Try Again";
}
?>
