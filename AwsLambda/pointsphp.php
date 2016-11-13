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
	// ---------------------------- OVER HERE: WE NEED TO ADJUST THIS SO THAT ONLY THE PERSON ASSOCIATED WITH THE POINT GETS THE TEXT-----------------
    $people = array(
        "+13236384021" => "Some stick"
    );

    // Loop through people. $number is a phone number, and $name is the name 
    foreach ($people as $number => $name) {
        $sms = $client->account->messages->create(
            // the number we are sending to
            $number,

            array(
                'from' => "+12138631089", 
                
				if (/*NEED TO PUT VARIABLE FOR POINTS>SOME POINT VALUE*/) {
					'body' => "Hello there! Your child, $name, has done a damn good job yo! Go buy it sum cake."
				} else if (/*NEED TO PUT VARIABLE FOR POINTS<SOME OTHER POINT VALUE*/) {
					'body' => "Hello there! Your child, $name, is being a naughty little stick. Please punish it fam."
				}
            )
        );
    }
