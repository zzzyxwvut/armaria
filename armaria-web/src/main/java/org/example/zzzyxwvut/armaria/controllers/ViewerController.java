package org.example.zzzyxwvut.armaria.controllers;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
//import java.nio.charset.StandardCharsets;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.zzzyxwvut.armaria.handlers.DefaultExceptionModelAndView;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.itextpdf.text.Document;
import com.itextpdf.text.io.RandomAccessSource;
import com.itextpdf.text.io.RandomAccessSourceFactory;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;

@Controller
@PreAuthorize("isAuthenticated()")
@SessionAttributes("tome")
public class ViewerController
{
	private final Logger logger	= LogManager.getLogger();
	private static final byte[] ownerWord;

	static {
		Random random	= new Random();
		byte[] word	= new byte[32];
		random.nextBytes(word);
		ownerWord	= word;
	}

	@ExceptionHandler(IllegalStateException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ModelAndView handle(HttpServletRequest request,
						IllegalStateException e)
	{
		return DefaultExceptionModelAndView.populate(request,
			HttpStatus.FORBIDDEN, new ModelAndView("error"), e);
	}

	@RequestMapping(value = "/viewer",
				produces = MediaType.APPLICATION_PDF_VALUE)
	public void viewer(@ModelAttribute("tome") String tome,
						HttpServletRequest request,
						HttpServletResponse response)
	{
		Document document	= null;
		PdfReader reader	= null;

		try	(OutputStream os		= response.getOutputStream();
			ByteArrayOutputStream stream	= new ByteArrayOutputStream()) {

			String path	= request.getServletContext()
						.getRealPath("/resources/library/") + tome;
			RandomAccessSourceFactory factory = new RandomAccessSourceFactory();
			factory.setUsePlainRandomAccess(Document.plainRandomAccess);
			RandomAccessSource source	= factory.createBestSource(path);
			reader	= new PdfReader(new RandomAccessFileOrArray(source),
							ViewerController.ownerWord);
			document	= new Document();
			PdfCopy copy	= new PdfCopy(document, stream);
//			byte[] userWord	= tome.getBytes(StandardCharsets.UTF_8);
			copy.setEncryption(null, ViewerController.ownerWord,
						PdfWriter.ALLOW_SCREENREADERS,
						PdfWriter.STANDARD_ENCRYPTION_128);
			document.open();
			copy.addDocument(reader);
			document.close();

			response.setHeader("Expires", "0");
			response.setHeader("Cache-Control",
				"must-revalidate, post-check=0, pre-check=0");
			response.setHeader("Pragma", "public");
			response.setContentType("application/pdf");
			response.setContentLength(stream.size());

			stream.writeTo(os);
			os.flush();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				if (reader != null)
					reader.close();
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	@ModelAttribute("tome")
	public String tome(@RequestParam(value = "tome",
						required = false) String tome)
	{
		if (tome == null)
			throw new IllegalStateException("Illegal resource access");

		return tome;
	}
}
