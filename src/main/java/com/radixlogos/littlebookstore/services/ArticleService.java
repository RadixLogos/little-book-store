package com.radixlogos.littlebookstore.services;

import com.radixlogos.littlebookstore.dto.ArticleDTO;
import com.radixlogos.littlebookstore.entities.Article;
import com.radixlogos.littlebookstore.repositories.ArticleRepository;
import com.radixlogos.littlebookstore.services.exceptions.DatabaseException;
import com.radixlogos.littlebookstore.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    @Transactional(readOnly = true)
    public Page<ArticleDTO> findAllArticles(Pageable pageable, ArticleDTO articleDTO){
        if(!articleDTO.name().isEmpty()){
            return articleRepository.findAllPagedByName(pageable,articleDTO.name()).map(ArticleDTO::fromArticle);
        } else {
            return articleRepository.findAllPaged(pageable).map(ArticleDTO::fromArticle);
        }

    }

    @Transactional(readOnly = true)
    public ArticleDTO findArticleById(Long articleId){
        if(articleRepository.findById(articleId).isEmpty()){
            throw new ResourceNotFoundException("Artigo não encontrado");
        }
        return ArticleDTO.fromArticle(articleRepository.findById(articleId).get());
    }

    @Transactional
    public ArticleDTO insertArticle(ArticleDTO articleDTO){
        var article = new Article();
        copyDTOToEntity(article,articleDTO);
        articleRepository.save(article);
        return ArticleDTO.fromArticle(article);
    }

    @Transactional
    public ArticleDTO updateArticle(Long articleId, ArticleDTO articleDTO){
        if(!articleRepository.existsById(articleId)){
            throw new ResourceNotFoundException("Livro não encontrado");
        }
        var article = articleRepository.findById(articleId).orElseThrow(()-> new ResourceNotFoundException("Livro não encontrado"));
        copyDTOToEntity(article,articleDTO);
        articleRepository.save(article);
        return ArticleDTO.fromArticle(article);
    }

    @Transactional
    public void deleteArticle(Long articleId){
        if(!articleRepository.existsById(articleId)){
            throw new ResourceNotFoundException("Livro não encontrado");
        }
        try{
        articleRepository.deleteById(articleId);

        }catch (DataIntegrityViolationException e){
            throw new DatabaseException("Falha de integridade referencial");
        }

    }

    private void copyDTOToEntity(Article article, ArticleDTO articleDTO){
        article.setName(articleDTO.name());
        article.setImgUrl(articleDTO.imgUrl());
        System.out.println(articleDTO.imgUrl());

        article.setPrice(articleDTO.price());
        article.setStockQuantity(articleDTO.stockQuantity());
        article.setImgUrl(articleDTO.imgUrl());
    }

}
