package com.github.eostermueller.perfSandbox.model;

import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SerializationUtil {

	public static final String TAG_NAME_ACCOUNTS = "Accounts";
	public static final String TAG_NAME_ACCOUNT = "Account";
	public static final String TAG_NAME_ACCOUNT_ID = "Aid";
	public static final String TAG_NAME_BALANCE = "Bal";
	public static final String TAG_NAME_BRANCH_ID = "Bid";
	public static final String TAG_NAME_FILLER = "Filler";
	
	public static final String TAG_NAME_TRANSACTION = "Tran";
	public static final String TAG_NAME_DELTA = "Delta";
	public static final String TAG_NAME_TIME = "MTime";
	public static final String TAG_NAME_HISTORY_ID = "Hid";
	public static final String TAG_NAME_TELLER_ID = "Tid";
	DocumentBuilderFactory m_docFactory = null;
	DocumentBuilder m_docBuilder = null;
	Document m_sqlDetailsDoc = null;
	private OutputStream m_outputStream = null;

	public SerializationUtil() throws ParserConfigurationException {
		m_docFactory = DocumentBuilderFactory.newInstance();
		m_docBuilder = m_docFactory.newDocumentBuilder();
	}

	public void serialize(Accounts accounts) throws TransformerException  {
		Document doc = m_docBuilder.newDocument();
		Element rootElement = doc.createElement(TAG_NAME_ACCOUNTS);
		doc.appendChild(rootElement);		

		for(Account a : accounts.getAccounts())
			writeAccount(doc, rootElement, a);
		
		writeToOutput(doc);
	} 

	private void writeToOutput(Document doc) throws TransformerException {
		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(getOutputStream());
 
		// Output to console for testing
		//StreamResult result = new StreamResult(System.out);
 
		transformer.transform(source, result);		
	}

	private void writeAccount(Document doc, Element requestEle, Account account) {
		
		Element accountEle = doc.createElement(TAG_NAME_ACCOUNT);
		requestEle.appendChild(accountEle);
		
		Element eleAccountId = doc.createElement(TAG_NAME_ACCOUNT_ID);
		eleAccountId.appendChild(doc.createTextNode(Long.toString(account.accountId)));
		accountEle.appendChild(eleAccountId);

		Element eleBalance = doc.createElement(TAG_NAME_BALANCE);
		eleBalance.appendChild(doc.createTextNode(Long.toString(account.balance)));
		accountEle.appendChild(eleBalance);

		Element eleBranchId = doc.createElement(TAG_NAME_BRANCH_ID);
		eleBranchId.appendChild(doc.createTextNode(Long.toString(account.branchId)));
		accountEle.appendChild(eleBranchId);

		if (account.filler !=null) {
			Element eleFiller = doc.createElement(TAG_NAME_FILLER);
			eleFiller.appendChild(doc.createTextNode(account.filler));
			accountEle.appendChild(eleFiller);
		}
		
		for(Transaction t : account.transactions)
			writeTransaction(doc, accountEle,t);
	}
	private void writeTransaction(Document doc, Element accountEle, Transaction t) {
		
		Element transactionEle = doc.createElement(TAG_NAME_TRANSACTION);
		accountEle.appendChild(transactionEle);
		
		Element eleAccountId = doc.createElement(TAG_NAME_ACCOUNT_ID);
		eleAccountId.appendChild(doc.createTextNode(Long.toString(t.accountId)));
		transactionEle.appendChild(eleAccountId);

		Element eleDelta = doc.createElement(TAG_NAME_DELTA);
		eleDelta.appendChild(doc.createTextNode(Long.toString(t.delta)));
		transactionEle.appendChild(eleDelta);

		Element eleBranchId = doc.createElement(TAG_NAME_BRANCH_ID);
		eleBranchId.appendChild(doc.createTextNode(Long.toString(t.branchId)));
		transactionEle.appendChild(eleBranchId);

		Element eleTime = doc.createElement(TAG_NAME_TIME);
		if (eleTime!=null && t.mtime !=null) {
			eleTime.appendChild(doc.createTextNode(t.mtime.toString()));
			transactionEle.appendChild(eleTime);
		}

		Element eleFiller = doc.createElement(TAG_NAME_FILLER);
		if (eleFiller !=null && t.filler !=null) {
			eleFiller.appendChild(doc.createTextNode(t.filler));
			transactionEle.appendChild(eleFiller);
		}
		
		Element eleHistoryId = doc.createElement(TAG_NAME_HISTORY_ID);
		if (eleHistoryId!=null) {
			eleHistoryId.appendChild(doc.createTextNode(Long.toString(t.historyId)));
			transactionEle.appendChild(eleHistoryId);
		}
		
		Element eleTellerId = doc.createElement(TAG_NAME_TELLER_ID);
		if (eleTellerId !=null) {
			eleTellerId.appendChild(doc.createTextNode(Long.toString(t.tellerId)));
			transactionEle.appendChild(eleTellerId);
		}

	}
	public OutputStream getOutputStream() {
		return m_outputStream;
	}

	public void setOutputStream(OutputStream val) {
		m_outputStream = val;
		
	}

}
