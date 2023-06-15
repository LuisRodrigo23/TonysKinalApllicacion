/*
	* Luis Rodrigo Vaquin
    * IN5BM
    * 2022300
    * Fecha de Creación: 28/03/23
    * Fecha de modificación: 
*/

Drop database if exists DBTonysKinal2023;

Create database DBTonysKinal2023;

Use DBTonysKinal2023;

Create table Empresas (
	codigoEmpresa int not null auto_increment,
    nombreEmpresa varchar(150) not null,
    direccion varchar(150) not null,
    telefono varchar(10) not null,
    
    primary key PK_codigoEmpresa (codigoEmpresa)
);	

Create table TipoEmpleado (
	codigoTipoEmpleado int not null auto_increment,
    descripcion varchar(50) not null,
    
    primary key PK_codigoTipoEmpleado (codigoTipoEmpleado)
);
Create table  Empleados (
	codigoEmpleado int not null auto_increment,
    numeroEmpleado int not null,
    apellidosEmpleado varchar(150) not null,
    nombresEmpleado varchar(150) not null,
    direccionEmpleado varchar(150) not null,
    telefonoContacto varchar(150) not null,
    gradoCocinero varchar(50),
    codigoTipoEmpleado int not null,
    
    primary key PK_codigoEmpleado (codigoEmpleado),
    
    constraint FK_Empleados_TipoEmpleado foreign key (codigoTipoEmpleado) 
		references TipoEmpleado (codigoTipoEmpleado) 
);

Create table TipoPlato (
	codigoTipoPlato int not null auto_increment,
    descripcionTipo varchar(100) not null,
    
    primary key PK_codigoTipoPlato (codigoTipoPlato)
);

Create table Productos (
	codigoProducto int not null auto_increment,
    nombreProducto varchar(150) not null,
    cantidad int not null,
    
    primary key PK_codigoProducto (codigoProducto)
);

Create table Servicios(
	codigoServicio int not null auto_increment,
    fechaServicio date not null,
    tipoServicio varchar(150) not null,
    horaServicio time not null,
    lugarServicio varchar(150) not null,
    telefonoContacto varchar(10) not null,
    codigoEmpresa int not null,
    
    primary key PK_codigoServicio(codigoServicio),
    constraint FK_Servicios_Empresas foreign key (codigoEmpresa)
		references Empresas (codigoEmpresa)
);

Create table Presupuestos(
	codigoPresupuesto int not null auto_increment,
    fechaSolicitud date not null,
    cantidadPresupuesto decimal(10,2),
    codigoEmpresa int not null,
    
    primary key PK_codigoPresupuesto(codigoPresupuesto),
    constraint FK_Presupuestos_Empresas foreign key (codigoEmpresa)
		references Empresas(codigoEmpresa)
);

Create table Platos(
	codigoPlato int not null auto_increment,
    cantidad int not null,
    nombrePlato varchar(50) not null,
    descripcionPlato varchar(150) not null,
    precioPlato decimal(10,2) not null,
    codigoTipoPlato int not null,
    -- TipoPlato codigoTipoPlato int not null
    
    primary key PK_codigoPlato(codigoPlato),
    constraint FK_Platos_TipoPlato1 foreign key (codigoTipoPlato)
		references TipoPlato (codigoTipoPlato)
);

Create table Productos_has_Platos(
	Productos_codigoProducto int not null,
    codigoPlato int not null,
    codigoProducto int not null,
    
    primary key PK_Productos_codigoProducto(Productos_codigoProducto),
    
    constraint FK_Productos_has_Platos_Productos1 foreign key (codigoProducto)
		references Productos(codigoProducto),
	constraint FK_Productos_has_Platos_Platos1 foreign key (codigoPlato)
		references Platos(codigoPlato)
);

Create table Servicios_has_Platos(
	Servicios_codigoServicio int not null,
    codigoPlato int not null,
    codigoServicio int not null,
    
    primary key PK_Servicios_codigoServicio(Servicios_codigoServicio),
    
    constraint FK_Servicios_has_Platos_Servicios1 foreign key (codigoServicio)
		references Servicios(codigoServicio),
	constraint FK_Servicios_has_Platos_Platos1 foreign key (codigoPlato)
		references Platos(codigoPlato)
);

Create table Servicios_has_Empleados(
	Servicios_codigoServicio int not null,
    codigoServicio int not null,
    codigoEmpleado int not null,
    fechaEvento date not null,
    horaEvento time not null,
    lugarEvento varchar(150) not null,
    
    primary key PK_Servicios_codigoServicio(Servicios_codigoServicio),
    
    constraint FK_Servicios_has_Empleados_Servicios1 foreign key (codigoServicio)
		references Servicios(codigoServicio),
	constraint FK_Servicios_has_Empleados_Empleados1 foreign key (codigoEmpleado)
		references Empleados(codigoEmpleado)
);


Create table Usuario(
	codigoUsuario int not null auto_increment,
    nombreUsuario varchar(100) not null,
    apellidoUsuario varchar(100) not null,
    usuarioLogin varchar (50) not null,
    contrasena varchar(50) not null,
    primary key PK_codigoUsuario (codigoUsuario)
);


Delimiter $$
	Create procedure sp_AgregarUsuario(in nombreUsuario varchar(100), in apellidoUsuario varchar(100),
	in usuarioLogin varchar(50), in contrasena varchar(50))
		begin 
			Insert into Usuario(nombreUsuario, apellidoUsuario, usuarioLogin, contrasena)
				values (nombreUsuario, apellidoUsuario, usuarioLogin, contrasena);
	End $$
Delimiter ;

call sp_AgregarUsuario ('Luis','Vaquin','luisrodrigo23','123');

Delimiter $$
	Create procedure sp_ListarUsuarios ()
    begin
		select * from Usuario;
	end $$
Delimiter ;

call sp_ListarUsuarios();

Create table Login (
	usuarioMaster varchar(50) not null,
    passwordLogin varchar(50) not null,
    primary key PK_usuarioMaster 	(usuarioMaster)
);



-- Use DBRecuperacion;

-- REPORTE GENERAL--
Delimiter $$
create procedure sp_ReporteGeneral()
begin
   SELECT 
		*
		FROM 
			Empresas e 
			INNER JOIN 	servicios s ON s.codigoempresa = e.codigoempresa
				INNER JOIN presupuestos p ON p.codigoempresa = e.codigoempresa;
					
                /*INNER JOIN servicios_has_empleados she ON she.codigoservicio = s.codigoservicio;
						/*INNER JOIN empleados emp ON she.codigoempleado = emp.codigoempleado
							INNER JOIN tipoempleado te ON te.codigoTipoEmpleado = emp.codigotipoempleado
								INNER JOIN servicios_has_platos shp ON shp.codigoservicio = s.codigoservicio
									INNER JOIN platos pla ON pla.codigoplato = shp.codigoplato
										INNER JOIN tipoplato tp ON tp.codigotipoplato = pla.codigotipoplato
											INNER JOIN productos_has_platos php ON php.codigoplato = pla.codigoplato
												INNER JOIN servicios_has_empleados she ON she.codigoservicio = s.codigoservicio;
			INNER JOIN productos pro ON pro.codigoproducto = php.codigoproducto;*/
end $$
Delimiter ;
call sp_ReporteGeneral();
-- ---------- Procedimientos almacenados Entidad Empresas ----------

-- Agregar Empresas---

Delimiter $$
	Create procedure sp_AgregarEmpresa(in nombreEmpresa varchar(150), in direccion varchar(150), in telefono varchar(10))
		Begin
			Insert into Empresas (nombreEmpresa, direccion, telefono)
				values (nombreEmpresa, direccion, telefono);
        End $$
Delimiter ; 

call sp_AgregarEmpresa('Campero', 'Ciudad de Guatemala', '12345678');
call sp_AgregarEmpresa('Farmacia Galeno', 'Chimaltenango zona 3', '47242803');
call sp_AgregarEmpresa('Kentucky Fried Chicken', 'Mixco zona 4', '24242424');
call sp_AgregarEmpresa('Fundacion Kinal', 'Ciudad de Guatemala','89451132');
call sp_AgregarEmpresa('Mac Donals','San Lucas zona 3','47420678');
-- Listar Empresas---

Delimiter $$
	Create procedure sp_ListarEmpresa()
		Begin
			Select * from Empresas;
        End $$
Delimiter ; 

call sp_ListarEmpresa();

-- Buscar Empresas---

Delimiter $$
	Create procedure sp_BuscarEmpresa(in codEmp int)
		Begin
			Select * from Empresas
				where codigoEmpresa = codEmp;
        End $$
Delimiter ; 

call sp_BuscarEmpresa(1);
call sp_BuscarEmpresa(2);
call sp_BuscarEmpresa(3);

-- Actualizar Empresas---

Delimiter $$
	Create procedure sp_ActualizarEmpresa(in codEmp int, in nomEmp varchar(150), in dir varchar(150), in tel varchar(10))
		Begin
			Update Empresas
				set nombreEmpresa = nomEmp,
					direccion = dir,
					telefono = tel
			where codigoEmpresa = codEmp;
        End $$	
Delimiter ; 


-- Eliminar Empresas---

Delimiter $$
	Create procedure sp_EliminarEmpresa(in codEmp int)
		Begin
			Delete from Empresas
				where codigoEmpresa= codEmp;
        End $$
Delimiter ; 


-- -------------------- Procedimientos Almacenados Entidad Tipo Empleado --------------------

-- Agregar Tipo Empleado

Delimiter $$
	Create procedure sp_AgregarTipoEmpleado(in descr varchar(150))
		Begin
			Insert into TipoEmpleado (descripcion)
				values (descr);
        End $$
Delimiter ; 

call sp_AgregarTipoEmpleado('Chef');
call sp_AgregarTipoEmpleado('Gourmet');
call sp_AgregarTipoEmpleado('Recepcionista');

-- Listar Tipo Empleado---

Delimiter $$
	Create procedure sp_ListarTipoEmpleado()
		Begin
			Select * from TipoEmpleado;
        End $$
Delimiter ; 

call sp_ListarTipoEmpleado();

-- Buscar Tipo Empleado---

Delimiter $$
	Create procedure sp_BuscarTipoEmpleado(in codTipEmp int)
		Begin
			Select * from TipoEmpleado
				where codigoTipoEmpleado = codTipEmp;
        End $$
Delimiter ; 

call sp_BuscarTipoEmpleado(1);
call sp_BuscarTipoEmpleado(2);
call sp_BuscarTipoEmpleado(3);

-- Actualizar Tipo Empleado---

Delimiter $$
	Create procedure sp_ActualizarTipoEmpleado(in codTipEmp int, in descr varchar(50))
		Begin
			Update TipoEmpleado
				set descripcion = descr
			where codigoTipoEmpleado = codTipEmp;
        End $$
Delimiter ; 


-- Eliminar Tipo Empleado---

Delimiter $$
	Create procedure sp_EliminarTipoEmpleado(in codTipEmp int)
		Begin
			Delete from TipoEmpleado
				where codigoTipoEmpleado= codTipEmp;
        End $$
Delimiter ; 


-- -------------------- Procedimientos Almacenados  Entidad Empleados --------------------

-- Agregar Empleado

Delimiter $$
	Create procedure sp_AgregarEmpleado(in numEmp int, in apEmp varchar(150), in nomEmp varchar(150), 
		in dirEmp varchar(150), in telCon varchar(150), in graCoc varchar(50), in codTipEmp int)
		Begin
			Insert into Empleados (numeroEmpleado, apellidosEmpleado, nombresEmpleado, direccionEmpleado, telefonoContacto, gradoCocinero, codigoTipoEmpleado)
				values (numEmp, apEmp, nomEmp, dirEmp, telCon, graCoc, codTipEmp );
        End $$
Delimiter ; 

call sp_AgregarEmpleado(101, 'Gamarro Perez', 'Juan Andrés', '8va Av. 9-96 Colonia San Fermin Z.21, Ciudad de Guatemala', '45781212', 'Experto en cortes de Res', 1);
call sp_AgregarEmpleado(102, 'Alvaro Carrillo', 'Zury Juliana', 'Calle Los Olivos al lado de Home Depot, Z.10, Guatemala', '34662210', 'Sous chef', 3);
call sp_AgregarEmpleado(103, 'Perez Orozco', 'Susana', 'Avenida La Cueva Z.4, Ciudad de Guatemala', '45781212', 'Aprendiz de salsas', 2);

-- Listar Tipo Empleado---
sp_ListarEmpleado
Delimiter $$
	Create procedure sp_ListarEmpleado()
		Begin
			Select * from Empleados;
        End $$
Delimiter ; 

call sp_ListarEmpleado();

-- Buscar Empleado---

Delimiter $$
	Create procedure sp_BuscarEmpleado(in codEmp int)
		Begin
			Select * from Empleados
				where codigoEmpleado = codEmp;
        End $$
Delimiter ; 

call sp_BuscarEmpleado(1);
call sp_BuscarEmpleado(2);
call sp_BuscarEmpleado(3);

-- Actualizar Tipo Empleado---
select * from Empleados;

Delimiter $$
	Create procedure sp_ActualizarEmpleado(in codEmp int, in numEmp int, in apEmp varchar(150), in nomEmp varchar(150), 
		in dirEmp varchar(150), in telCon varchar(150), in graCoc varchar(50), in codTipEmp int)
		Begin
			Update Empleados
				set numeroEmpleado = numEmp, 
					apellidosEmpleado = apEmp, 
                    nombresEmpleado = nomEmp, 
                    direccionEmpleado = dirEmp, 
                    telefonoContacto = telCon, 
                    gradoCocinero = graCoc, 
                    codigoTipoEmpleado = codTipEmp
			where codigoEmpleado = codEmp;
        End $$
Delimiter ; 


-- Eliminar Tipo Empleado---

Delimiter $$
	Create procedure sp_EliminarEmpleado(in codEmp int)
		Begin
			Delete from Empleados
				where codigoEmpleado= codEmp;
        End $$
Delimiter ; 


-- -------------------- Procedimientos Almacenados Entidad Tipo Plato --------------------

-- Agregar Tipo Plato

Delimiter $$
	Create procedure sp_AgregarTipoPlato(in descr varchar(100))
		Begin
			Insert into TipoPlato (descripcionTipo)
				values (descr);
        End $$
Delimiter ; 

call sp_AgregarTipoPlato('Ensalada');
call sp_AgregarTipoPlato('Sopa');
call sp_AgregarTipoPlato('Plato Fuerte');

-- Listar Tipo Plato

Delimiter $$
	Create procedure sp_ListarTipoPlato()
		Begin
			Select * from TipoPlato;
        End $$
Delimiter ; 

call sp_ListarTipoPlato();

-- Buscar Tipo Plato

Delimiter $$
	Create procedure sp_BuscarTipoPlato(in codTipPlat int)
		Begin
			Select * from TipoPlato
				where codigoTipoPlato = codTipPlat;
        End $$
Delimiter ; 

call sp_BuscarTipoPlato(1);
call sp_BuscarTipoPlato(2);
call sp_BuscarTipoPlato(3);

-- Actualizar Tipo Plato

Delimiter $$
	Create procedure sp_ActualizarTipoPlato(in codTipPlat int, in descr varchar(100))
		Begin
			Update TipoPlato
				set descripcionTipo = descr
			where codigoTipoPlato = codTipPlat;
        End $$
Delimiter ; 


-- Eliminar Tipo Plato

Delimiter $$
	Create procedure sp_EliminarTipoPlato(in codTipPlat int)
		Begin
			Delete from TipoPlato
				where codigoTipoPlato= codTipPlat;
        End $$
Delimiter ; 

-- -------------------- Procedimientos Almacenados Entidad Productos --------------------

-- Agregar Productos

Delimiter $$
	Create procedure sp_AgregarProductos(in nomProd varchar(150), in cant int)
		Begin
			Insert into Productos (nombreProducto, cantidad)
				values (nomProd, cant);
        End $$
Delimiter ; 

call sp_AgregarProductos('Tomate', 250);
call sp_AgregarProductos('Cebolla', 198);
call sp_AgregarProductos('Papa', 302);

-- Listar Productos

Delimiter $$
	Create procedure sp_ListarProductos()
		Begin
			Select * from Productos;
        End $$
Delimiter ; 

call sp_ListarProductos();

-- Buscar Productos

Delimiter $$
	Create procedure sp_BuscarProductos(in codProd int)
		Begin
			Select * from Productos
				where codigoProducto = codProd;
        End $$
Delimiter ; 

call sp_BuscarProductos(1);
call sp_BuscarProductos(2);
call sp_BuscarProductos(3);

-- Actualizar Productos

Delimiter $$
	Create procedure sp_ActualizarProductos(in _codigoProducto int, in _nombreProducto Varchar(150), in _cantidad int)
		Begin
			Update Productos
				set nombreProducto = _nombreProducto,
                    cantidad = _cantidad
			where codigoProducto = _codigoProducto;
        End $$
Delimiter ; 


/*Delimiter $$
	Create procedure sp_ActualizarEmpresa(in codEmp int, in nomEmp varchar(150), in dir varchar(150), in tel varchar(10))
		Begin
			Update Empresas
				set nombreEmpresa = nomEmp,
					direccion = dir,
					telefono = tel
			where codigoEmpresa = codEmp;
        End $$	
Delimiter ; */


-- Eliminar Productos

Delimiter $$
	Create procedure sp_EliminarProductos(in codProd int)
		Begin
			Delete from Productos
				where codigoProducto = codProd;
        End $$
Delimiter ; 


-- -------------------- Procedimientos Almacenados Entidad sp_EliminarServiciosServicios ----------

-- Agregar Servicios

Delimiter $$
	Create procedure sp_AgregarServicios(in fecSer date, in tipSer varchar(150), in horaSer time, in lugSer varchar(150), in telCon varchar(10), in codEmp int)
		Begin
			Insert into Servicios (fechaServicio, tipoServicio, horaServicio, lugarServicio, telefonoContacto, codigoEmpresa)
				values (fecSer, tipSer, horaSer, lugSer, telCon, codEmp);
        End $$
Delimiter ; 

call sp_AgregarServicios('2023-05-03', 'Chef', '15:16:35', '82 Calle 99-99 Colonia El Pepe Z.48 al lado de Home Depot', '45321100', 1);
call sp_AgregarServicios('2023-05-04', 'Cortador de Vegetales', '20:30:55', 'Edificio Eventicos z.12, Guatemala', '26335512', 3);
call sp_AgregarServicios('2023-05-05', 'Salsero', '16:16:16', 'Colonia Roma a la par de Gorditas Eloid 99-21, Guatemala', '32004455', 3);

-- Listar Servicios

Delimiter $$
	Create procedure sp_ListarServicios()
		Begin
			Select * from Servicios;
        End $$
Delimiter ; 

call sp_ListarServicios();

-- Buscar Servicios

Delimiter $$
	Create procedure sp_BuscarServicios(in codSer  int)
		Begin
			Select * from Servicios
				where codigoServicio = codSer;
        End $$
Delimiter ; 

call sp_BuscarServicios(1);
call sp_BuscarServicios(2);
call sp_BuscarServicios(3);

-- Actualizar Servicios

Delimiter $$
	Create procedure sp_ActualizarServicios(in codSer int, in fecSer date, in tipSer varchar(150), in horSer time, in lugSer varchar(150), in telCon varchar(10), in codEmp int)
		Begin
			Update Servicios
				set fechaServicio = fecSer, 
					tipoServicio = tipSer, 
                    horaServicio = horSer, 
                    lugarServicio = lugSer, 
                    telefonoContacto = telCon, 
                    codigoEmpresa = codEmp
			where codigoServicio = codSer;
        End $$
Delimiter ; 

-- Eliminar Servicios

Delimiter $$
	Create procedure sp_EliminarServicios(in codProd int)
		Begin
			Delete from Servicios
				where codigoServicio = codSer;
        End $$
Delimiter ; 


-- -------------------- Procedimientos Almacenados Presupuestos --------------------

-- Agregar Presupuestos

Delimiter $$
	Create procedure sp_AgregarPresupuestos(in fechSoli date, in cantPres decimal(10,2), in codEmp int)
		Begin
			Insert into Presupuestos (fechaSolicitud, cantidadPresupuesto, codigoEmpresa)
				values (fechSoli, cantPres, codEmp);
        End $$
Delimiter ; 

call sp_AgregarPresupuestos('2023-05-03', 15000.00, 1);
call sp_AgregarPresupuestos('2023-06-04', 25000.00, 2);
call sp_AgregarPresupuestos('2023-07-05', 30000.00, 3);

-- Listar Presupuestos

Delimiter $$
	Create procedure sp_ListarPresupuestos()
		Begin
			Select * from Presupuestos;
        End $$
Delimiter ; 

call sp_ListarPresupuestos();

-- Buscar Presupuestos

Delimiter $$
	Create procedure sp_BuscarPresupuestos(in codPres  int)
		Begin
			Select * from Presupuestos
				where codigoPresupuesto = codPres;
        End $$
Delimiter ; 

call sp_BuscarPresupuestos(1);
call sp_BuscarPresupuestos(2);
call sp_BuscarPresupuestos(3);

-- Actualizar Presupuestos

Delimiter $$
	Create procedure sp_ActualizarPresupuestos(in codPres int, in fechSoli date, in cantPres decimal(10,2), in codEmp int)
		Begin
			Update Presupuestos
				set fechaSolicitud = fechSoli, 
					cantidadPresupuesto = cantPres, 
                    codigoEmpresa = codEmp
			where codigoPresupuesto = codPres;
        End $$
Delimiter ; 
sp_ListarPlatossp_ActualizarProductossp_ActualizarProductos

-- Eliminar Presupuestos

Delimiter $$
	Create procedure sp_EliminarPresupuestos(in codPres int)
		Begin
			Delete from Presupuestos
				where codigoPresupuesto = codPres;
        End $$
Delimiter ; 


-- -------------------- Procedimientos Almacenados Platos --------------------

-- Agregar Platos

Delimiter $$
	Create procedure sp_AgregarPlatos(in cant int, in nomPlat varchar(50), in descriPlat varchar(150), in preciPlat decimal(10,2), in codTipoPlato int)
		Begin
			Insert into Platos (cantidad, nombrePlato, descripcionPlato, precioPlato, codigoTipoPlato)
				values (cant, nomPlat, descriPlat, preciPlat, codTipoPlato);
        End $$
Delimiter ; 

call sp_AgregarPlatos(60, 'Pasta Diavola', 'Espagueti con crema picante y camarones', 65.50, 3);
call sp_AgregarPlatos(32, 'Crema de Papa', 'Reducción de Papa con crema y especias de la casa', 40.00, 2);
call sp_AgregarPlatos(100, 'Esnalada Pesto', 'Vegetales frescos salteados con salsa Pesto en una cama de acelga', 37.50, 1);

-- Listar Platos

Delimiter $$
	Create procedure sp_ListarPlatos()
		Begin
			Select * from Platos;
        End $$
Delimiter ; 

call sp_ListarPlatos();

-- Buscar Platos

Delimiter $$
	Create procedure sp_BuscarPlatos(in codPlat  int)
		Begin
			Select * from Platos
				where codigoPlato = codPlat;
        End $$
Delimiter ; 

call sp_BuscarPlatos(1); 
call sp_BuscarPlatos(2);
call sp_BuscarPlatos(3);

-- Actualizar Platos

Delimiter $$
	Create procedure sp_ActualizarPlatos(in codPlat int, in canti int, in nomPlat varchar(50), in descriPlat varchar(150), in preciPlat decimal(10,2), in codTipoPlato int)
		Begin
			Update Platos
				set cantidad = canti, 
					nombrePlato = nomPlat, 
                    descripcionPlato = descriPlat, 
                    precioPlato = preciPlat, 
                    codigoTipoPlato = codTipoPlato
			where codigoPlato = codPlat;
        End $$
Delimiter ; 


-- Eliminar Platos

Delimiter $$
	Create procedure sp_EliminarPlatos(in codPlat int)
		Begin
			Delete from Platos
				where codigoPlato = codPlat;
        End $$
Delimiter ; 

-- ------------------------------------
Delimiter $$
	Create procedure sp_ListarServicios_has_Empleados()
    begin 
		select * from servicios_has_empleados;
	end $$
Delimiter ;

-- ---------------------------------------------------------------------------------------------
Delimiter //
	create procedure sp_AgregarServicio_has_Empleado(in Servicios_codigoServicio int, in codigoServicio int,
		in codigoEmpleado int , in fechaEvento date ,in  horaEvento time, in lugarEvento varchar(150))
        Begin
			Insert into Servicios_has_Empleados(Servicios_codigoServicio, codigoServicio, 
				codigoEmpleado, fechaEvento, horaEvento, lugarEvento) values 
                (Servicios_codigoServicio, codigoServicio, codigoEmpleado, fechaEvento, horaEvento, lugarEvento);
        End//
Delimiter ;

call sp_AgregarServicio_has_Empleado(1, 1, 1, '2023-05-10', '12:30:00', "Ciudad Cayala");
call sp_AgregarServicio_has_Empleado(2, 1, 2, '2023-07-13', '11:30:00', "Ciudad Cayala");
call sp_AgregarServicio_has_Empleado(3, 3, 1, '2023-08-11', '09:32:00', "Ciudad Cayala");

call sp_ListarServicios_has_Empleados();


Delimiter $$
	Create procedure sp_ListarProductos_has_Platos()
		begin 
			select * from productos_has_platos;
		end $$
Delimiter ;
call sp_ListarProductos_has_Platos();

Delimiter $$
	Create procedure sp_AgregarProducto_has_Plato(in Productos_codigoProducto int, in codigoPlato int, in codigoProducto int)
        begin
			Insert Into Productos_has_Platos(Productos_codigoProducto, codigoPlato, codigoProducto)
				Values (Productos_codigoProducto, codigoPlato, codigoProducto);
        end $$
Delimiter ; 

call sp_AgregarProducto_has_Plato(1, 1, 1);
call sp_AgregarProducto_has_Plato(2, 3, 1);