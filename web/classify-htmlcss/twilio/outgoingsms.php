<?php 
 
// Get the PHP helper library from twilio.com/docs/php/install 
require_once '/path/to/vendor/autoload.php'; // Loads the library 
 
use Twilio\Rest\Client; 
 
$account_sid = 'ACbf8126ea1f615a890f506afff793b7f5'; 
$auth_token = '6e7ffb6c971918f36bdd591cf290bc9e'; 
$client = new Client($account_sid, $auth_token); 
 
$messages = $client->accounts("ACbf8126ea1f615a890f506afff793b7f5") 
  ->messages->create("+13236384021", array( 
        'From' => "+12138631089", 
        'MessagingServiceSid' => "MGe11fa0ddb23c3f60dbabb350925c4369", 
        'Body' => "why it no work",      
  ));