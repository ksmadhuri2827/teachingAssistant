<!DOCTYPE html>
<html>

<head>
    <meta name="viewport" content="width=device-width initial-scale=1">
    <link href="css/bootstrap.css" rel="stylesheet" />
    <meta name="viewport" content="width=device-width" />
    <link href="https://fonts.googleapis.com/css?family=Roboto&display=swap" rel="stylesheet">
    <script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
    <link rel="stylesheet" type="text/css"
        href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.css">
    <style>
        #registerLink {
            color: white;
        }
        label {
            color: black;
        }

        .form-group>input {
            width: 60%;
        }

        .form-group>select {
            width: 60%;
        }

        .jumbotron {
            width: 70%;
            padding-top: 3%;
            padding-bottom: 3%;
            border-radius: 30px;
        }

        .container {
            border-radius: 5%;
        }

        .container-fluid {
            /* height:20%; */
            padding: 1.5% 2%;
        }

        .navbar {
            width: 100%;
            padding: 0%;
            margin-bottom: 3%;
        }

        .loginBtn {
            background-color: #302244 !important;
        }
        
    </style>
</head>

<body>
    <center>
        <nav class="navbar navbar-light bg-light">
            <div class="container-fluid" style="background-color:#036;">
                <span class="navbar-brand mb-0 h1" style="color:white;">SIGN IN</span>
            </div>
        </nav>
        <!-- <h1 style="margin-top:4%;color:#533b78;"></h1><br> -->
        <div class="container">
            <div class="jumbotron" style="background-color: #d9ecff;">
                <form name="login" id="loginForm">
                    <div class="form-group">
                        <label for="username" >ZNumber or Email</label>
                        <input type="email" class="form-control" id="username" required="true" name="username"
                            placeholder="name@example.com">
                    </div>
                    <div class="form-group">
                        <label for="password">Password</label>
                        <input type="password" class="form-control" id="password" required="true" name="password"
                            placeholder="password">
                    </div>
                    <div class="form-group">
                        <label for="usertype" >User type</label>
                        <select class="form-control" id="usertype">
                            <option value="none">-- select --</option>
                            <option value="applicant">Applicant</option>
                            <option value="admin">Administrator</option>
                            <option value="committee">TA Committee</option>
                            <option value="instructor">Instructor</option>
                        </select>
                    </div>
                    <br>
                    <br>
                    <p style="color:black;font-size: small;">
                        NOTE: Please select appropriate user type in the above dropdown
                    </p>
                    <!-- <br> -->
                    <div>
                        <a href="#register" style="color:black;font-weight:800;" id="registerLink">No account ? Register now</a>
                    </div>
                    <br>
                    <button type="submit" name="loginBtn" class="btn btn-primary" id="loginBtn"
                        style="background-color: #036;color:white;">Login</button>
                </form>
            </div>
        </div>
    </center>
    <script>
        $(document).ready(function () {
            $('#loginForm').click(function (event) {
                event.preventDefault();
            });
            $('#register').click(function () {
                window.location.href = "register.jsp";
            });
        });
        $("#registerLink").click(function () {
            window.location.href = "register.jsp";
        })
        $("#loginBtn").click(function () {
            var usertype = document.login.usertype.value;
            if (document.login.usertype.value != "" || document.login.usertype.value != "none") {
                $.ajax({
                    type: "GET",
                    url: "login",
                    data: { username: $("#username").val(), password: $("#password").val(), usertype: $("#usertype").val() },
                    success: function (result) {
                        if (result == "incorrect") {
                            alert("Incorrect credentials!!");
                        }
                        else {
                            if (usertype == "applicant") {
                                window.location.href = "applicantHome";
                            } else if (usertype == "admin") {
                                window.location.href = "adminHome";
                            } else if (usertype == "instructor") {
                                window.location.href = "instructorHome";
                            } else if (usertype == "committee") {
                                window.location.href = "committeeHome";
                            } else {
                                alert("Error");
                            }
                        }
                    }
                });
            }
            else {
                alert("Enter all details");
            }

        });
    </script>
</body>

</html>