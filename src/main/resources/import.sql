insert into collections(name, description) values("alimento", "Primario comestible.");
insert into collections(name, description) values("contenedor", "Objeto contenedor como caja, bandeja, frasco, etc.");

insert into primaries(name, budget, collection_id) values("jugo de naranja", 1500, 1);
insert into primaries(name, budget, collection_id) values("frasco", 1850.0, 2);

insert into elements(name, description, collection_id) values("jugo de naranja", "delicioso jugo de naranja", 1);

