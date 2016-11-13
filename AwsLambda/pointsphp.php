<?php
    // NEED TO: Get the Twilio-PHP library from twilio.com/docs/libraries/php, following the instructions to install it with Composer.
    require_once "vendor/autoload.php";
    use Twilio\Rest\Client;
    
    // Our account info
    $AccountSid = "ACbf8126ea1f615a890f506afff793b7f5";
    $AuthToken = "6e7ffb6c971918f36bdd591cf290bc9e";

    // Instantiate a new Twilio Rest Client
    $client = new Client($AccountSid, $AuthToken);

    // Array of people
	$arlength = $_GET["length"];
	$name1 = $_GET["getName"];
    $people = array(
        "+13236384021" => "$name1" //+17854774265
    );

    // Loop through people. $number is a phone number, and $name is the name 
    foreach ($people as $number => $name) {
        $sms = $client->account->messages->create(
            // the number we are sending to
            $number,

            array(
                'from' => "+12138631089", 
                for ($i=0; $i<$arlength; $i++) {
					$na = "getPoints[".$i."]";
					$points = $_GET[$na];
					
					if ($points>=90) {
						'body' => "Hello there! Your child,".$name.", has done a damn good job yo! They have accumulated ".$points." points. Go buy it sum cake."
					} else if ($points<=10) {
						'body' => "Hello there! Your child,".$name.", is being a naughty little stick. They have ".$points." points at the moment. Please punish it fam."
					}
				}
				
				
            )
        );
    }
