create table article
(
    article_id bigint        not null primary key,
    title      varchar(100)  not null,
    content    varchar(3000) not null,
    board_id   bigint        not null,
    writer_id  bigint        not null,
    created_at datetime      not null,
    updated_at datetime      not null
);

create index idx_board_id_article_id on article (board_id asc, article_id desc);

CREATE TABLE article_file
(
    article_file_id    BIGINT       NOT NULL PRIMARY KEY,
    article_id         BIGINT       NOT NULL,
    file_url           VARCHAR(512) NOT NULL,
    original_filename  VARCHAR(255) NOT NULL,
    file_size          BIGINT,
    file_type          VARCHAR(100),
    created_at         DATETIME(6)  NOT NULL,
    CONSTRAINT fk_file_to_article FOREIGN KEY (article_id) REFERENCES article (article_id) ON DELETE CASCADE
);

CREATE INDEX idx_article_file_on_article_id ON article_file (article_id);

create table board_article_count
(
    board_id      bigint not null primary key,
    article_count bigint not null
);
