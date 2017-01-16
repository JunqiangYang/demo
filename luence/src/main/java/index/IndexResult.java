/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package index;

/**
 *实体类的类型都为string类型，都为将要显示的类型
 * @author Administrator
 */
public class IndexResult {
    private String id; 
    
    private String page_id;
    
    private String file_name; 
  
    private String file_size;  
  
    private String file_pages;  

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFile_name() {
        return file_name;
    }

    public String getPage_id() {
        return page_id;
    }

    public void setPage_id(String page_id) {
        this.page_id = page_id;
    }
    
    
    
    
    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getFile_size() {
        return file_size;
    }

    public void setFile_size(String file_size) {
        this.file_size = file_size;
    }

    public String getFile_pages() {
        return file_pages;
    }

    public void setFile_pages(String file_pages) {
        this.file_pages = file_pages;
    }

   

    public IndexResult() {
    }   
    
}
