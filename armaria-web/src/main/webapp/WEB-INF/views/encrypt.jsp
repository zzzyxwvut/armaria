<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8" name="viewport" content="width=device-width, initial-scale=1.0" />
		<title>Encrypt!</title>
	</head>

	<body style="background: #bebebe">
		<h2 align="center"><i>Under construction...</i></h2><br><br><br><br>

		<div align="center">
			<form action="encrypt" method="post" style="border: 0.2ex outset #ffffff;
							box-sizing: border-box; width: 30%">
				<p><input type="text" name="plain" value="${plain}" size="18"
					autofocus autocomplete="off" placeholder="plain text"
					style="text-align: center" /></p>

				<p><textarea rows="4" cols="26" readonly placeholder="encrypted text"
					style="text-align: center">${encrypted}</textarea></p>

				<p><input type="submit" value="Encrypt!" /></p>
			</form>
		</div>
	</body>
</html>
