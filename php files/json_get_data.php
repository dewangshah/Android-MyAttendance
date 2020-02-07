<?php

$db_name="myattendance";
$mysql_user="root";
$mysql_pass="";
$server_name="localhost";

$sql="select * from studsub;";

$con=mysqli_connect($server_name,$mysql_user,$mysql_pass,$db_name);

$result=mysqli_query($con,$sql);

$response=array();

while($row=mysqli_fetch_array($result))
{
array_push($response,array("id"=>$row[0],"stud_id"=>$row[1],"dwm"=>$row[2],"dwmt"=>$row[3],"hmi"=>$row[4],"hmit"=>$row[5],"pds"=>$row[6],"pdst"=>$row[7],"ml"=>$row[8],"mlt"=>$row[9],"name"=>$row[10],"image"=>$row[11]));

}

echo json_encode(array("server_response"=>$response));

mysqli_close($con);

?>