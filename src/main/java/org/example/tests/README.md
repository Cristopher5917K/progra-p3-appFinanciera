# Tests - Asistencia Financiera

Este package contiene los tests para validar que el código funcione correctamente.

## 📋 Tests disponibles

### ConexionesTest
Valida que las funciones de conexión a la BD funcionen correctamente:

- **TEST 1**: `testGetConnection()` - Verifica que la conexión MySQL se establezca
- **TEST 2**: `testUserInfoById()` - Valida que se obtiene correctamente la info del usuario por ID
- **TEST 3**: `testUserProfileInfo()` - Valida que se obtiene correctamente el perfil del usuario

## 🚀 Cómo ejecutar los tests

### Opción 1: Ejecutar desde IDE
1. Click derecho en `ConexionesTest.java`
2. Seleccionar "Run 'ConexionesTest.main()'"

### Opción 2: Desde línea de comandos
```bash
mvn test
```

## ✅ Qué validar antes de hacer Push

Asegúrate de que:
1. ✅ Los 3 tests pasen correctamente
2. ✅ No haya errores en la consola
3. ✅ La conexión a BD funcione
4. ✅ Los usuarios se obtengan correctamente

## ⚠️ Si los tests fallan

Si algún test falla:
1. Revisa el mensaje de error en la consola
2. Verifica que tu `.env` tenga las credenciales correctas
3. Verifica que la BD MySQL esté corriendo
4. Verifica que exista el usuario con ID 1

## 🔄 GitHub Actions

Cuando hagas `push` o `pull request`:
- GitHub ejecuta automáticamente estos tests
- Si fallan, ❌ bloquea el merge
- Debes arreglarlo y hacer push de nuevo

## 📝 Agregar nuevos tests

Para agregar más tests:
1. Abre `ConexionesTest.java`
2. Crea un nuevo método público `testTuNuevaFuncion()`
3. Llámalo desde `main()`
4. Sigue el patrón: `System.out.println("TEST X: ...")` + try/catch

