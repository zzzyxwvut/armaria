package org.example.zzzyxwvut.armaria.servlets;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.zzzyxwvut.tomcat.resources.LibrarianBean;

/**
 * This class provides a means to reference a path on the file system.
 */
public class LibrarianServlet extends HttpServlet
{
	private static final long serialVersionUID	= -2893488840321237557L;
	private final Logger logger	= LogManager.getLogger();

	@Override
	public void init(ServletConfig config) throws ServletException
	{
		LibrarianBean bean	= null;

		try {
			Context initContext	= new InitialContext();
			Context envContext	= (Context) initContext.lookup("java:comp/env");
			bean	= (LibrarianBean) envContext.lookup("bean/LibrarianBeanFactory");
		} catch (NamingException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}

		String libraryName	= Objects.requireNonNull(bean.getLibraryName());
		String fileSuffix	= Objects.requireNonNull(bean.getFileSuffix());
		File targetDir		= new File(System.getProperty("user.home"),
									libraryName);
		String targetDirName	= targetDir.toString() + "/";
		String linkDirName	= config.getServletContext()
						.getRealPath("/resources/");
		Path target	= Paths.get(targetDirName);
		Path link	= Paths.get(linkDirName);

		try {
			if (!Files.isDirectory(target) || !Files.isDirectory(link)
					|| Files.isSameFile(target, link))
				throw new IllegalArgumentException();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}

		linkDirName	+= target.toFile().getName() + "/";
		link		= Paths.get(linkDirName);

		/*
		 * (1) Try to make a symbolic link between the source and
		 *	the target directories.
		 *
		 * Note that the source directory might require a dummy index.html
		 *	(see Tomcat. The Definitive Guide, $3, Symbolic Links)
		 */
		try {
			if (!Files.exists(link))
				Files.createSymbolicLink(link, target);

			return;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		try {	/* Create the target directory. */
			if (!Files.isDirectory(link))
				Files.createDirectory(link);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}

		/* Fetch the fully-qualified file names to link or copy. */
		File[] list	= targetDir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File directory, String name)
			{
				return (new File(directory + "/" + name)).isFile()
					&& name.toLowerCase().endsWith(fileSuffix);
			}
		});
		boolean passed	= true;

		/*
		 * (2) Try to make symbolic links between the files of
		 *	the source and the target directories.
		 */
		for (File file : list) {
			target	= Paths.get(targetDirName, file.getName());
			link	= Paths.get(linkDirName, file.getName());

			try {
				if (!Files.exists(link))
					Files.createSymbolicLink(link, target);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				passed	= false;
				break;
			}
		}

		if (passed)
			return;

		/*
		 * (3) Try to make copies of the source directory files
		 *	into the target directory.
		 */
		for (File file : list) {
			Path source	= file.toPath();
			target		= Paths.get(linkDirName, file.getName());

			try {
				if (!Files.exists(target))
					Files.copy(source, target);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new RuntimeException(e);
			}
		}
	}
}
