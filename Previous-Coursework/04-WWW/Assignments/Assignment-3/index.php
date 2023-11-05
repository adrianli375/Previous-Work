<?php
define("DB_HOST", "mydb");
define("USERNAME", "dummy");
define("PASSWORD", "c3322b");
define("DB_NAME", "db3322");
$conn=mysqli_connect(DB_HOST, USERNAME, PASSWORD, DB_NAME) or die('Error! '. mysqli_connect_error($conn));
$cookieExpireTime = 5 * 60;

function databaseConnectionError() {
    http_response_code(500);
    header('Content-Type: application/json');
    $response = array('error' => 'Internal Server Error');
    echo json_encode($response);
}

if ($_SERVER['REQUEST_METHOD'] === 'GET') {
    // remove outdated entries
    $query = 'delete from cookies where timestamp < '.time();
    $result = mysqli_query($conn, $query) or die ('Failed to query '.$query.mysqli_error($conn));
    // initialize the order for html output
    $order = [1, 2, 3, 4, 5, 6, 7, 8];
    // set cookies
    // if no cookies present, send a new cookie
    if (!isSet($_COOKIE['uid'])) {
        $cookieName = 'uid';
        $cookieValue = 'idd'.str_pad(rand(1, 99999), 5, '0', STR_PAD_LEFT);
        $expirationTime = time() + $cookieExpireTime;
        setcookie($cookieName, $cookieValue);
        // add the new cookie to the db
        $visible = '"blk1", "blk2", "blk3", "blk4", "blk5", "blk6", "blk7", "blk8"';
        $hidden = '';
        $query = 'insert into cookies values (\''.$cookieValue.'\', \''.$visible.'\', \''.$hidden.'\', '.$expirationTime.')';
        $result = mysqli_query($conn, $query) or die ('Failed to query '.$query.mysqli_error($conn));
    }
    else {
        // check if the cookie exists in the database
        $query = 'select count(*) as maxid from cookies where uid = \''.$_COOKIE['uid'].'\'';
        $result = mysqli_query($conn, $query) or die('Failed to query '.$query.mysqli_error($conn));
        while($row = mysqli_fetch_array($result)) {
            $count = $row['maxid'];
            break;
        }
        // if no cookie exists, send a new cookie
        if (($count == 0) || ($count > 0 && ((intval($_COOKIE['uid']) - time()) > $cookieExpireTime))) {
            // remove the old cookie from the db
            $query = 'delete from cookies where uid = \''.$_COOKIE['uid'].'\'';
            $result = mysqli_query($conn, $query) or die ('Failed to query '.$query.mysqli_error($conn));
            // send a new cookie
            $cookieName = 'uid';
            $cookieValue = 'idd'.str_pad(rand(1, 99999), 5, '0', STR_PAD_LEFT);
            $expirationTime = time() + $cookieExpireTime;
            setcookie($cookieName, $cookieValue);
            // add the new cookie to the db
            $visible = '"blk1", "blk2", "blk3", "blk4", "blk5", "blk6", "blk7", "blk8"';
            $hidden = '';
            $query = 'insert into cookies values (\''.$cookieValue.'\', \''.$visible.'\', \''.$hidden.'\', '.$expirationTime.')';
            $result = mysqli_query($conn, $query) or die ('Failed to query '.$query.mysqli_error($conn));
        }
        else {
            // get the UID of the client, query if the client has a custom display setting
            $query = 'select visible from cookies where uid = \''.$_COOKIE['uid'].'\'';
            $result = mysqli_query($conn, $query) or die ('Failed to query '.$query.mysqli_error($conn));
            $visible = mysqli_fetch_array($result)['visible'];
            // rearrange the content of the HTML if needed
            $pattern = '/blk(\d)/';
            if (preg_match_all($pattern, $visible, $matches)) {
                $order = array_map('intval', $matches[1]);
            }
        }
    }
    // prepare the output of the html
    $html = '';
    $htmlBeginning = <<<EOD
    <!doctype html>
    <html>
    <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="index.css">
    <script src="jquery-3.7.1.js"></script>
    <script src="index.js"></script>
    <title>Financial Dashboard</title>
    </head>
    <body>
        <div id="outmost">
            <h1>Financial Dashboard</h1>
            <div id='container'>
    EOD;
    $containers = [
        <<<EOD
        <div>
            <h2>Block 1 - SP500</h2>
            <p><img src="images/SP500.png" alt="SP500" style="width: 250px;">
            </p>
        </div>
        EOD, 
        <<<EOD
        <div>
            <h2>Block 2 - FTSE 100</h2>
            <p><img src="images/FTSE100.png" alt="FTSE 100">
            </p>
        </div>
        EOD,
        <<<EOD
        <div>
            <h2>Block 3 - Hang Seng Index</h2>
            <p><img src="images/HSI.png" alt="Hang Seng Index" style="width: 900px;">
            </p>
        </div>
        EOD,
        <<<EOD
        <div>
            <h2>Block 4 - Nasdaq Composite index</h2>
            <p><img src="images/nasdaq.png" alt="NASDAQ" style="width: 600px;">
            </p>
        </div>
        EOD,
        <<<EOD
        <div>
            <h2>Block 5 - USD Exchange Rate</h2>
            <p><img src="images/ex_rate.png" alt="USD Rate" style="width: 250px;">
            </p>
        </div>
        EOD,
        <<<EOD
        <div>
            <h2>Block 6 - Currency Converter</h2>
            <p><img src="images/Convert-Currency.png" alt="Currency Converter" style="width: 300px;">
            </p>
        </div>
        EOD,
        <<<EOD
        <div>
            <h2>Block 7 - Crypto Index</h2>
            <p><img src="images/Crypto.png" alt="Crypto Index" style="width: 500px;">
            </p>
        </div>
        EOD,
        <<<EOD
        <div>
            <h2>Block 8 - USD vs. HKD</h2>
            <p><img src="images/USD-HKD.png" alt="USD vs. HKD" style="width: 400px;">
            </p>
        </div>
        EOD
    ];
    $htmlContainer = '';
    $htmlEnd = <<<EOD
        </div>
        </div>
    </body>
    </html>
    EOD;
    // get htmlContainer based on order
    foreach ($order as $idx) {
        $htmlContainer = $htmlContainer.$containers[$idx - 1];
    }
    $html = $htmlBeginning.$htmlContainer.$htmlEnd;
    echo $html;
}
else if ($_SERVER['REQUEST_METHOD'] === 'PUT') {
    if (!isSet($_COOKIE['uid']) || !isSet($_COOKIE['visible']) || !isSet($_COOKIE['hidden'])) {
        http_response_code(400);
        header('Content-Type: application/json');
        $response = array('error' => 'Bad Request');
        echo json_encode($response);
    }
    // clear expired cookies
    $query = 'delete from cookies where timestamp < '.time();
    $result = mysqli_query($conn, $query) or die (databaseConnectionError());
    // get cookie information from browser
    $uid = $_COOKIE['uid'];
    $visible = $_COOKIE['visible'];
    $hidden = $_COOKIE['hidden'];
    $query = 'select count(*) as maxid from cookies where uid = \''.$uid.'\'';
    $result = mysqli_query($conn, $query) or die(databaseConnectionError());
    while($row = mysqli_fetch_array($result)) {
        $count = $row['maxid'];
        break;
    }
    // uid does not exist in the database
    if ($count == 0) {
        $cookieName = 'uid';
        $cookieValue = 'idd'.str_pad(rand(1, 99999), 5, '0', STR_PAD_LEFT);
        $expirationTime = time() + $cookieExpireTime;
        $query = 'insert into cookies values (\''.$cookieValue.'\', \''.$visible.'\', \''.$hidden.'\', '.$expirationTime.')';
        $result = mysqli_query($conn, $query) or die(databaseConnectionError());
        $uid = $cookieValue;
        http_response_code(200);
        echo $uid;
    }
    // uid exists in the database
    else {
        // check if the cookie expires
        $query = 'select count(*) as maxid from cookies where uid = \''.$uid.'\' and timestamp > '.time();
        $result = mysqli_query($conn, $query) or die(databaseConnectionError());
        while($row = mysqli_fetch_array($result)) {
            $count = $row['maxid'];
            break;
        }
        // if the cookie expires
        if ($count == 0) {
            // delete the existing cookie
            $query = 'delete from cookies where uid = \''.$uid.'\'';
            // make a new cookie
            $cookieName = 'uid';
            $cookieValue = 'idd'.str_pad(rand(1, 99999), 5, '0', STR_PAD_LEFT);
            $expirationTime = time() + $cookieExpireTime;
            $query = 'insert into cookies values (\''.$cookieValue.'\', \''.$visible.'\', \''.$hidden.'\', '.$expirationTime.')';
            $uid = $cookieValue;
        }
        // if the cookie is not yet expired
        else {
            $query = 'update cookies set visible = \''.$visible.'\', hidden = \''.$hidden.'\' where uid = \''.$uid.'\'';
        }
        $result = mysqli_query($conn, $query) or die(databaseConnectionError());
        http_response_code(200);
        echo $uid;
    }
}
?>