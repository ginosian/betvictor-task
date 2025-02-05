CREATE TABLE processed_words
(
    id                            BIGSERIAL PRIMARY KEY,
    freq_word                     VARCHAR(255),
    avg_paragraph_size            INT,
    avg_paragraph_processing_time DOUBLE PRECISION,
    total_processing_time         DOUBLE PRECISION,
    created                       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated                       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted                       BOOLEAN   DEFAULT FALSE NOT NULL
);
