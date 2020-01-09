package xyz.cintia_quentin.projet;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;




public class BaseDeTweets {
	// Déclaration des var nécéssaires
    private StandardAnalyzer analyzer;
    private Directory index;
    private Query query;
    private IndexSearcher indexSearcher;
    private ScoreDoc[] hits;
    private IndexReader reader;
    
    
    public BaseDeTweets(String databasePath) {
    	analyzer = new StandardAnalyzer();
    	try {
			index = new SimpleFSDirectory(Paths.get(databasePath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
    
	public void LoadFileWithTweets(String filepath) throws IOException{
		IndexWriterConfig config = new IndexWriterConfig(this.analyzer);
		IndexWriter writer = new IndexWriter(this.index, config);
		int i =0;
		FileInputStream fis = new FileInputStream(filepath);
		try (BufferedReader br = new BufferedReader(new InputStreamReader(fis, "UTF8"))) {
			// Lecture ligne a ligne
			String line = br.readLine();
			while (line != null) {
				if(i%1000==0)
					System.out.println(i + " lignes chargées.");
				InsertTweet(writer,line);
				line = br.readLine();
				i++;
			}
			
		}
		writer.close();
	}
		
	public void InsertTweet(IndexWriter indexWriter, String newtweet) {
		Document doc = new Document();

		String splitString[]=newtweet.split("\t");
		
		if(splitString.length<4) 
			return;
		
		doc.add(new StringField("id_tweet", splitString[0], Field.Store.YES));
		doc.add(new StringField("id_user", splitString[1], Field.Store.YES));
		doc.add(new StringField("mydate", splitString[2], Field.Store.YES));
		doc.add(new TextField("content", splitString[3], Field.Store.YES));
		if(splitString.length==5) { 
			doc.add(new StringField("rt_user", splitString[4], Field.Store.YES));
		}
		
		try {
			indexWriter.addDocument(doc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
		
	public void SearchTweet(String queryString) {
		try {
			query = new QueryParser("content", analyzer).parse(queryString);
			int hitsPerPage = 500;
			reader = DirectoryReader.open(index);
			indexSearcher = new IndexSearcher(reader);
			TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage);
			indexSearcher.search(query, collector);
			hits = collector.topDocs().scoreDocs;
			
			System.out.println("Query was : " + queryString);
			System.out.println("Found : " + hits.length + " results.");
			
			for (int i = 0; i < hits.length; ++i) {
				int docId = hits[i].doc;
				Document d = indexSearcher.doc(docId);
				System.out.println((i + 1) + ". " + d.get("id_user") + "\t" + d.get("content"));
				}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	public StringBuilder printListContent() throws IOException {
        /*StringBuilder collection_content = new StringBuilder();
        this.gettweetBase().forEach((var) -> {
            collection_content.append(var.toString());
        });
        return collection_content;
    	StringBuilder collection_content = new StringBuilder();
    	IndexReader reader = DirectoryReader.open(FSDirectory("tIndex"));
    			for (int i=0; i<reader.maxDoc(); i++) {
    			    Document doc;
					doc = reader.document(i);
					String docId = doc.get("docId");
					collection_content.append(docId.toString());
    			}
    			return collection_content;
    	
    }*/
	

    
}
