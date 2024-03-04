
-- DELETE STEP
delete from step 
where recipe_id in (
	select recipe_id 
	from recipe 
	where  recipe_name 
	like '테스트요리%' and description like '정말 맛있는 요리입니다!! 짱짱%');

-- DELETE INGREDIENT
delete from ingredient where recipe_id in (
	select recipe_id 
	from recipe 
	where  recipe_name 
	like '테스트요리%' and description like '정말 맛있는 요리입니다!! 짱짱%');


-- DELETE RECIPE
delete from recipe 
where recipe_name 
like '테스트요리%' and description like '정말 맛있는 요리입니다!! 짱짱%';

-- DELETE USER
delete from user where user_id like '%test%';

