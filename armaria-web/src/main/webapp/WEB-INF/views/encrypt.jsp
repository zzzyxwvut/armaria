<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8" name="viewport" content="width=device-width, initial-scale=1.0" />
		<title>Encrypt!</title>
	</head>

	<body style="background: #bebebe">
		<hr>
		<br><br><br><br>
		<div align="center">
			<form action="encryptor" method="post" style="border: 0.2ex outset #ffffff;
							box-sizing: border-box; width: 30%">
				<p><textarea rows="4" cols="26" placeholder="plain text"
					name="plain" autofocus autocomplete="off"
					style="text-align: center">${plain}</textarea></p>
				<p><textarea rows="4" cols="26" readonly placeholder="encrypted text"
					style="text-align: center">${encrypted}</textarea></p>

				<p><input type="submit" value="Encrypt!" /></p>
			</form>
		</div>
	</body>
</html>
