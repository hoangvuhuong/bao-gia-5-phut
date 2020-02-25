package adstech.vn.quotationbackoffice.controller;

import static com.itextpdf.text.pdf.BaseFont.EMBEDDED;
import static com.itextpdf.text.pdf.BaseFont.IDENTITY_H;
import static org.thymeleaf.templatemode.TemplateMode.HTML;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.FileSystems;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.fasterxml.jackson.databind.ObjectMapper;

//import adstech.vn.quotationbackoffice.contract.DataQuotation;
import adstech.vn.quotationbackoffice.contract.DataQuotationReport;
import adstech.vn.quotationbackoffice.service.IQuotationService;
import adstech.vn.quotationbackoffice.util.CommonConstant;

@Controller
public class PDFGenerator {
	@Autowired
	IQuotationService quotationService;
	
	private static final String UTF_8 = "UTF-8";
	private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss");  
    
    @GetMapping(path="/quotation")
	public void downloadPDF(@RequestParam String n, HttpServletResponse response) throws IOException{
    	
    	String[] paths = n.split("/");
    	String filename = "baogia.pdf";
    	if (paths.length > 1) {
    		filename = paths[paths.length - 1];
    	}
    	
    	response.setContentType("application/pdf");
	    //response.setHeader("Content-disposition","attachment;filename="+ "baogia.pdf");
	    response.setHeader("Content-disposition","inline;filename="+ filename);
	    try {
	        File f = new File(CommonConstant.QUOTATION_FOLDER + File.separator + n);
	        FileInputStream fis = new FileInputStream(f);
	        DataOutputStream os = new DataOutputStream(response.getOutputStream());
	        response.setHeader("Content-Length",String.valueOf(f.length()));
	        byte[] buffer = new byte[1024];
	        int len = 0;
	        while ((len = fis.read(buffer)) >= 0) {
	            os.write(buffer, 0, len);
	        }
	        fis.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
    }
    
//    @PostMapping(path="/actions/doit")
//	public ResponseEntity<String> downloadQuotationPDF(HttpServletRequest request, @RequestBody Object newQuotation, Principal principal){
//    	ObjectMapper mapper = new ObjectMapper();
//    	DataQuotation data = mapper.convertValue(newQuotation, DataQuotation.class);
//    	try {			
//			return ResponseEntity.ok(generatePdf(data, principal.getName()));
//		} catch (Exception e) {
//			e.printStackTrace();
//			return new ResponseEntity<String>("ERR", HttpStatus.INTERNAL_SERVER_ERROR);
//		}    	
//    }
	
    @PostMapping(path="/actions/exportReport")
    public ResponseEntity<String> downloadQuotationPDFreport(HttpServletRequest request, @RequestBody Object newQuotation, Principal principal){
    	ObjectMapper mapper = new ObjectMapper();
    	DataQuotationReport data = mapper.convertValue(newQuotation, DataQuotationReport.class);
    	
    	data.setQuotation(quotationService.getQuotation(data.getQuotationId()));
    	try {			
			return ResponseEntity.ok(generatePdf(data, principal.getName()));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("ERR", HttpStatus.INTERNAL_SERVER_ERROR);
		}    	
    }
	public String generatePdf(DataQuotationReport data, String username) throws Exception {

        // We set-up a Thymeleaf rendering engine. All Thymeleaf templates
        // are HTML-based files located under "src/test/resources". Beside
        // of the main HTML file, we also have partials like a footer or
        // a header. We can re-use those partials in different documents.
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/pdf/default/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(HTML);
        templateResolver.setCharacterEncoding(UTF_8);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        data.setPaginationKeyWord();
        data.paginationKeyword();
        // The data in our Thymeleaf templates is not hard-coded. Instead,
        // we use placeholders in our templates. We fill these placeholders
        // with actual data by passing in an object. In this example, we will
        // write a letter to "John Doe".
        //
        // Note that we could also read this data from a JSON file, a database
        // a web service or whatever.
        //Data data = exampleDataForJohnDoe();

        Context context = new Context();
        context.setVariable("data", data);
        
        // Flying Saucer needs XHTML - not just normal HTML. To make our life
        // easy, we use JTidy to convert the rendered Thymeleaf template to
        // XHTML. Note that this might no work for very complicated HTML. But
        // it's good enough for a simple letter.
        String renderedHtmlContent = templateEngine.process("template", context);
        String xHtml = convertToXhtml(renderedHtmlContent);

        ITextRenderer renderer = new ITextRenderer();
        renderer.getFontResolver().addFont("templates/pdf/default/Code39.ttf", IDENTITY_H, EMBEDDED);
        renderer.getFontResolver().addFont("templates/pdf/default/OpenSans-Regular.ttf", IDENTITY_H, EMBEDDED);
        renderer.getFontResolver().addFont("templates/pdf/default/Roboto-Regular.ttf", IDENTITY_H, EMBEDDED);

        // FlyingSaucer has a working directory. If you run this test, the working directory
        // will be the root folder of your project. However, all files (HTML, CSS, etc.) are
        // located under "/src/test/resources". So we want to use this folder as the working
        // directory.
        String baseUrl = FileSystems
                                .getDefault()
                                .getPath("src", "main", "resources", "templates", "pdf", "default")
                                .toUri()
                                .toURL()
                                .toString();
        renderer.setDocumentFromString(xHtml, baseUrl);
        renderer.layout();

        //String fileName = "/Users/namng/Working/" + UUID.randomUUID().toString() + ".pdf";
        
        File uploadRootDir = new File(CommonConstant.QUOTATION_FOLDER + File.separator + username);
        String fileName = data.getQuotation().getPartnerName().replace(" ", "_") + "_" + dateFormat.format(new Date()) + ".pdf";
        String res = username + File.separator + fileName;
		if (!uploadRootDir.exists()) {
			uploadRootDir.mkdirs();
		}
		String name = uploadRootDir.getAbsolutePath() + File.separator + fileName;
        
        // And finally, we create the PDF:
        OutputStream outputStream = new FileOutputStream(name);
        renderer.createPDF(outputStream);
        outputStream.close();
        quotationService.updateQuotationLinkReport(data.getQuotationId(), "/quotation?n="+res);
        return res;
    }

    private String convertToXhtml(String html) throws UnsupportedEncodingException {
        Tidy tidy = new Tidy();
        tidy.setInputEncoding(UTF_8);
        tidy.setOutputEncoding(UTF_8);
        tidy.setXHTML(true);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes(UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        tidy.parseDOM(inputStream, outputStream);
        return outputStream.toString(UTF_8);
    }
}
