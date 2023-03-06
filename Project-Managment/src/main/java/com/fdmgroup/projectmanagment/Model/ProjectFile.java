package com.fdmgroup.projectmanagment.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "project_files")
public class ProjectFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;
    
    @Column(name="file_type")
    private String fileType ;
    
    @ManyToOne
    private Project project ;

    @Lob
    @Column(name = "data" ,columnDefinition = "LONGBLOB" )
    private byte[] data;

	
	public ProjectFile(String name, String fileType , byte[] data) {
		super();
		this.name = name;
		this.data = data;
		this.fileType = fileType ;
	}

    
   
}