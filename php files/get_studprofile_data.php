<?php

require "init.php";
$studid=$_POST["studid"];
$sql="select * from studsub where stud_id like '$studid';";

$result=mysqli_query($con,$sql);

$response=array();

while($row=mysqli_fetch_array($result))
{
array_push($response,array("id"=>$row[0],"stud_id"=>$row[1],"dwm"=>$row[2],"dwmt"=>$row[3],"hmi"=>$row[4],"hmit"=>$row[5],"pds"=>$row[6],"pdst"=>$row[7],"ml"=>$row[8],"mlt"=>$row[9],"name"=>$row[10],"image"=>$row[11]));

}

echo json_encode(array("server_response"=>$response));

mysqli_close($con);

?>