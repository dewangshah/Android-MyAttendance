<?php
require "init.php";
$user_name="uday_bhave";

$sql_query="select image from hod where username like '$user_name';";

$result=mysqli_query($con,$sql_query);

if(mysqli_num_rows($result)>0)
{
   $row=mysqli_fetch_assoc($result);
   $name=$row["image"];
   header("content-type:image/jpeg");
   echo $name;
}
else
{
   echo "Login Failed, Try Again";
}
?>
