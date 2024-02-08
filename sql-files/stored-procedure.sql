use moviedb;
drop procedure if exists add_movie;

DELIMITER //
CREATE PROCEDURE add_movie(
	IN movie_title varchar(100),
    IN movie_year integer,
    IN movie_director varchar(100),
    IN star_name varchar(100),
    IN star_birthYear integer,
    IN genre_name varchar(32)
)
BEGIN
	DECLARE new_movie_id varchar(10);
	DECLARE new_star_id varchar(10);
	DECLARE new_genre_id int;
    DECLARE status varchar(100);
    
    main_block: Begin
    
    select id into new_movie_id
    from movies
    where title = movie_title and year = movie_year and director = movie_director;
    
    if new_movie_id is not null then
		SELECT 'Duplicate' INTO status;
        SELECT null INTO new_movie_id;
        SELECT null INTO new_star_id;
        SELECT null INTO new_genre_id;
		select new_movie_id, new_star_id, new_genre_id, status;
        leave main_block;
	else
		select concat(SUBSTRING(max(id), 1, 2), CAST(SUBSTRING(max(id), 3) AS UNSIGNED) + 1) into new_movie_id
		from movies;
        SELECT 'Success' INTO status;
    end if;
    
    select id into new_star_id
    from stars
    where name = star_name
    limit 1;
    
    if new_star_id is null then
		select concat(SUBSTRING(max(id), 1, 2), CAST(SUBSTRING(max(id), 3) AS UNSIGNED) + 1) into new_star_id
		from stars; 
        
		insert into stars
		values(new_star_id, star_name, star_birthyear); 
    end if;
    
    select id into new_genre_id
    from genres
    where name = genre_name
    limit 1;
    
    if new_genre_id is null then
		select max(id)+1 into new_genre_id
		from genres;
        
		insert into genres
		values(new_genre_id, genre_name);
    end if;
    
    insert into movies 
	values (new_movie_id, movie_title, movie_year, movie_director);

    insert into stars_in_movies
    values (new_star_id, new_movie_id);
    
    insert into genres_in_movies
    values (new_genre_id, new_movie_id);
    
    
    select new_movie_id, new_star_id, new_genre_id, status;
    
	END main_block;
END //
DELIMITER ;
