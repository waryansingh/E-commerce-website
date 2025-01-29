<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error 404 - Page Not Found</title>
    <style>
        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }

        body {
            font-family: Arial, sans-serif;
            background-color: #1e1e1e;
            color: #fff;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            background: linear-gradient(135deg, #ff8c00, #000);
        }

        .container {
            text-align: center;
            background-color: #2c2c2c;
            padding: 40px;
            border-radius: 15px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.5);
            width: 60%;
            max-width: 700px;
        }

        h1 {
            font-size: 80px;
            color: #ff8c00;
            margin-bottom: 10px;
        }

        h2 {
            font-size: 24px;
            color: #fff;
            margin-bottom: 20px;
        }

        p {
            font-size: 18px;
            color: #ccc;
            margin-bottom: 40px;
        }

        a {
            display: inline-block;
            padding: 15px 30px;
            font-size: 16px;
            background-color: #ff8c00;
            color: #000;
            border: none;
            border-radius: 5px;
            text-decoration: none;
            transition: background-color 0.3s ease;
        }

        a:hover {
            background-color: #ff5719;
        }

        .icon {
            font-size: 100px;
            color: #ff8c00;
            margin-bottom: 20px;
        }

        /* Responsive Design */
        @media (max-width: 768px) {
            h1 {
                font-size: 60px;
            }

            h2 {
                font-size: 20px;
            }

            p {
                font-size: 16px;
            }

            a {
                padding: 12px 25px;
                font-size: 14px;
            }
        }
    </style>
</head>
<body>

    <div class="container">
        <div class="icon">⚠️</div>
        <h1>404</h1>
        <h2>Oops! Page Not Found</h2>
        <p><%="Invalid Request"%></p>
        <a href="index">Go Back to Homepage</a>
    </div>

</body>
</html>
