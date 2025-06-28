INSERT INTO students (full_name, kana_name, nickname, age, gender, email, address, remark, isDeleted)
VALUES ('モンキー・D・ルフィ', 'モンキーディールフィ', 'ニカ', 19, '男性', 'rubber@example.com', 'フーシャ村', NULL, 0),
       ('ロロノア・ゾロ', 'ロロノアゾロ', 'マリモ', 21, '男性', 'swords@example.com', 'シモツキ村', NULL, 0),
       ('ナミ', 'ナミ', 'ナミさーん', 20, '女性', 'orange@example.com', 'ココヤシ村', NULL, 0),
       ('ウソップ', 'ウソップ', 'そげキング', 19, '男性', 'sniper@example.com', 'シロップ村', NULL, 0),
       ('ヴィンスモーク・サンジ', 'ヴィンスモークサンジ', '素敵マユゲ', 21, '男性', 'cook@example.com', 'ノース・ブルー', NULL, 0),
       ('トニートニー・チョッパー', 'トニートニーチョッパー', 'たぬき', 17, 'トナカイ', 'rumble@example.com', 'ドラム王国', NULL, 0),
       ('ニコ・ロビン', 'ニコロビン', 'ロビン', 30, '女性', 'fleur@example.com', 'オハラ', NULL, 0),
       ('フランキー', 'フランキー', '鉄人', 36, 'サイボーグ', 'super@example.com', 'サウス・ブルー', NULL, 0),
       ('ブルック', 'ブルック', 'ソウルキング', 90, 'ホネ', 'yohohoho@example.com', 'ウエスト・ブルー', NULL, 0),
       ('ジンベエ', 'ジンベエ', '海侠', 46, 'サメ', 'taiyo@example.com', 'リュウグウ王国', NULL, 0);

INSERT INTO students_courses (course_name, student_id, start_date, end_date)
VALUES ('AWSフル', 1, '2024-04-01', '2034-03-31'),
       ('Javaフル', 2, '2024-05-01', '2034-04-30'),
       ('デザイン', 3, '2024-06-01', '2034-05-31'),
       ('Webマーケティング', 4, '2024-07-01', '2034-06-30'),
       ('WordPress副業', 5, '2024-08-01', '2034-07-31'),
       ('フクリー', 2, '2024-10-01', '2034-09-30'),
       ('転職クエスト', 1, '2025-06-09', '2026-06-09');
