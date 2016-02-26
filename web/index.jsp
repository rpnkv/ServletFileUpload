<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>File upload</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css"
          integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"
            integrity="sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS"
            crossorigin="anonymous"></script>
    <script src="scr/init.js" type="text/javascript"></script>
</head>
<body>
<jsp:useBean id="calendar" class="java.util.GregorianCalendar"/>
<jsp:useBean id="ob" scope="request" class="test.Message" />
<jsp:setProperty name="ob" property="text" value="Hello))))"/>
<div class="content">
    <div class="container p-t-10">
        <div class="panel panel-color panel-primary">
            <div class="panel-heading">
                <h3 class="panel-title"> Tempest keep was merely a setback </h3>
            </div>
            <div class="panel-body">
                <form name="Simple" action="timeaction" method="POST" enctype="multipart/form-data">
                    <input type="file" name="file">
                    <input type="hidden" name="time" value="${calendar.timeInMillis}"/>
                    <input type="submit" name="button" value="Посчитать время"/>
                </form>

                <p>
                    Message text: ${ob.text}
                </p>
            </div>
        </div>
    </div>
</div>
</body>
</html>
