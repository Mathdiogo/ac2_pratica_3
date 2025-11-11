# 📚 Documentação Técnica - AC2 DevOps

## 1️⃣ Camada Entity e Value Objects - Recursos Lombok

### 🔧 Anotações Lombok Utilizadas

#### **@Getter**
```java
@Getter
public class Aluno {
    private Long id;
    private String nome;
}
```

**Importância:**
- ✅ **Reduz boilerplate**: Elimina necessidade de escrever métodos getters manualmente
- ✅ **Manutenibilidade**: Mudanças nos atributos não requerem atualização manual dos getters
- ✅ **Legibilidade**: Código mais limpo e focado na lógica de negócio
- ✅ **Padrão JavaBeans**: Mantém compatibilidade com frameworks (JPA, Jackson, etc.)

**O que gera:**
```java
public Long getId() {
    return this.id;
}

public String getNome() {
    return this.nome;
}
```

---

#### **@ToString**
```java
@ToString(of = {"id", "nome", "registroAcademico"})
public class Aluno {
    // atributos
}
```

**Importância:**
- ✅ **Debugging**: Facilita visualização do estado do objeto durante debug
- ✅ **Logs**: Permite logging estruturado do objeto completo
- ✅ **Customizável**: Pode excluir campos sensíveis (ex: senhas)
- ✅ **Performance**: Evita loops infinitos com `exclude` em relacionamentos bidirecionais

**O que gera:**
```java
@Override
public String toString() {
    return "Aluno(id=" + this.id + ", nome=" + this.nome + 
           ", registroAcademico=" + this.registroAcademico + ")";
}
```

**Exemplo de uso:**
```java
Aluno aluno = Aluno.criar(nome, ra);
logger.info("Aluno criado: {}", aluno); // Usa toString() automaticamente
// Output: Aluno(id=1, nome=João Silva, registroAcademico=RegistroAcademico(valor=12345ABC))
```

---

#### **@EqualsAndHashCode**
```java
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Aluno {
    @EqualsAndHashCode.Include
    private Long id;
    private String nome;
}
```

**Importância:**
- ✅ **Collections**: Permite usar objetos em HashSet, HashMap corretamente
- ✅ **Comparação de objetos**: Compara por valor, não por referência
- ✅ **Testes**: Essencial para assertThat().isEqualTo() funcionar
- ✅ **JPA**: Correto funcionamento de cache de segundo nível do Hibernate
- ✅ **Previne bugs**: Evita comparação por referência (==) em lógica de negócio

**O que gera:**
```java
@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Aluno aluno = (Aluno) o;
    return Objects.equals(id, aluno.id);
}

@Override
public int hashCode() {
    return Objects.hash(id);
}
```

**Exemplo de uso:**
```java
Aluno aluno1 = repository.findById(1L);
Aluno aluno2 = repository.findById(1L);

// Sem equals/hashCode: false (diferentes referências)
// Com equals/hashCode: true (mesmo ID)
assertThat(aluno1).isEqualTo(aluno2);

// Funciona em Collections
Set<Aluno> alunos = new HashSet<>();
alunos.add(aluno1);
alunos.add(aluno2); // Não duplica porque ID é igual
assertThat(alunos).hasSize(1);
```

---

### 🎯 **Por que usar `onlyExplicitlyIncluded = true` na Entity?**

```java
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Aluno {
    @EqualsAndHashCode.Include  // Apenas ID participa do equals/hashCode
    private Long id;
    
    // Estes campos NÃO participam do equals/hashCode
    private String nome;
    private double mediaFinal;
}
```

**Motivos:**
1. **Entidades JPA devem comparar por ID** (chave primária)
2. **Evita problemas com lazy loading** do Hibernate
3. **Permite atualizar campos** sem quebrar equals (ex: alterar nome mantém mesma entidade)
4. **Performance**: Compara apenas 1 campo ao invés de todos

---

### 📦 **Value Objects - Comparação por Valor**

```java
@Getter
@ToString
@EqualsAndHashCode  // SEM onlyExplicitlyIncluded
public class RegistroAcademico {
    private final String valor;
}
```

**Diferença para Entities:**
- ✅ **Todos os campos participam** do equals/hashCode
- ✅ **Imutáveis** (final fields)
- ✅ **Sem ID**: São comparados por seus valores, não por identidade

**Exemplo:**
```java
RegistroAcademico ra1 = RegistroAcademico.criar("12345ABC");
RegistroAcademico ra2 = RegistroAcademico.criar("12345ABC");

assertThat(ra1).isEqualTo(ra2); // TRUE - mesmo valor
assertThat(ra1 == ra2);          // FALSE - objetos diferentes
```

---

### ⚠️ **Setters - Por que NÃO usamos @Setter?**

```java
// ❌ NÃO FAZEMOS ISSO:
@Getter
@Setter
public class Aluno {
    private String nome;
}

// ✅ FAZEMOS ISSO:
@Getter
public class Aluno {
    private String nome;
    
    public void alterarNome(NomeAluno novoNome) {
        if (novoNome == null) {
            throw new DomainException("Nome não pode ser nulo");
        }
        this.nome = novoNome.getValor();
        this.dataAtualizacao = LocalDateTime.now();
    }
}
```

**Motivos:**
1. **Validações**: Métodos de negócio validam antes de alterar
2. **Encapsulamento**: Controla COMO o estado muda
3. **Auditoria**: Atualiza campos como `dataAtualizacao`
4. **Regras de negócio**: Impede alterações inválidas (ex: mudar RA após conclusão)

---

## 2️⃣ Padrão JPA e ORM

### Anotações JPA na Entity Aluno

```java
@Entity
@Table(name = "tb_alunos")
@Getter
@ToString(of = {"id", "nome", "registroAcademico"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Aluno {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nome;
    
    @Embedded
    @AttributeOverride(name = "valor", column = @Column(name = "ra", unique = true))
    private RegistroAcademico registroAcademico;
    
    @Column(nullable = false)
    private double mediaFinal;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao;
    
    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        dataAtualizacao = LocalDateTime.now();
    }
}
```

**Explicação das Anotações:**

| Anotação | Propósito |
|----------|-----------|
| `@Entity` | Marca a classe como entidade JPA |
| `@Table(name)` | Define nome da tabela no banco |
| `@Id` | Define chave primária |
| `@GeneratedValue` | Gera ID automaticamente (auto-increment) |
| `@Column` | Configura coluna (nullable, unique, length, etc.) |
| `@Embedded` | Incorpora Value Object na mesma tabela |
| `@AttributeOverride` | Customiza mapeamento de campo embedded |
| `@PrePersist` | Callback executado antes de INSERT |
| `@PreUpdate` | Callback executado antes de UPDATE |

---

## 3️⃣ Configuração de Profiles

### 📂 Estrutura de Arquivos

```
src/main/resources/
├── application.properties         # Configurações gerais
├── application-dev.properties     # Profile de Desenvolvimento
└── application-test.properties    # Profile de Testes
```

### ⚙️ application.properties (Principal)

```properties
spring.profiles.active=dev

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# JPA
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true

# Swagger
springdoc.swagger-ui.path=/swagger-ui.html
```

### 🔧 application-dev.properties

```properties
# Banco H2 em memória
spring.datasource.url=jdbc:h2:mem:devdb

# Logs detalhados
logging.level.com.devops.projeto_ac2=DEBUG
```

### 🧪 application-test.properties

```properties
# Banco isolado para testes
spring.datasource.url=jdbc:h2:mem:testdb

# Logs mínimos
logging.level.org.springframework=WARN
```

**Como usar:**
```bash
# Rodar com profile dev (padrão)
mvn spring-boot:run

# Rodar com profile test
mvn spring-boot:run -Dspring-boot.run.profiles=test
```

---

## 4️⃣ H2 Console - Visualizar Schema do Banco

### 🌐 Acessar H2 Console

1. **Iniciar aplicação:**
   ```bash
   mvn spring-boot:run
   ```

2. **Abrir navegador:**
   ```
   http://localhost:8080/h2-console
   ```

3. **Configurar conexão:**
   - **JDBC URL:** `jdbc:h2:mem:alunosdb`
   - **User Name:** `sa`
   - **Password:** (deixar vazio)

4. **Clicar em "Connect"**

### 📊 Schema Gerado pelo ORM

```sql
CREATE TABLE tb_alunos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    ra VARCHAR(20) NOT NULL UNIQUE,
    media_final DOUBLE NOT NULL,
    concluiu BOOLEAN NOT NULL,
    cursos_adicionais INTEGER NOT NULL,
    tentativas_avaliacao INTEGER NOT NULL,
    data_criacao TIMESTAMP NOT NULL,
    data_atualizacao TIMESTAMP,
    data_conclusao TIMESTAMP
);
```

### 🔍 Queries úteis no H2 Console

```sql
-- Ver todos os alunos
SELECT * FROM tb_alunos;

-- Ver schema da tabela
SHOW COLUMNS FROM tb_alunos;

-- Contar alunos aprovados
SELECT COUNT(*) FROM tb_alunos WHERE media_final >= 7.0 AND concluiu = true;

-- Ranking por média
SELECT nome, ra, media_final, cursos_adicionais 
FROM tb_alunos 
ORDER BY media_final DESC, cursos_adicionais DESC;
```

---

## 5️⃣ DTOs (Data Transfer Objects)

### 📦 Por que usar DTOs?

| Motivo | Explicação |
|--------|------------|
| **Desacoplamento** | API não expõe estrutura interna da Entity |
| **Segurança** | Não expõe campos sensíveis (ex: senha) |
| **Versionamento** | Pode ter várias versões de DTO para mesma Entity |
| **Validação** | Bean Validation nas requisições |
| **Serialização** | Controle total sobre JSON/XML gerado |

### Exemplo de DTO

```java
@Data
@Builder
public class AlunoResponseDTO {
    private Long id;
    private String nome;
    private String ra;
    private double mediaFinal;
    private boolean concluiu;
    private int cursosAdicionais;
    private int tentativasAvaliacao;
    private int tentativasRestantes;
    private String situacao;  // "APROVADO", "REPROVADO", etc.
    private LocalDateTime dataCriacao;
}
```

**Vantagens:**
- ✅ Não expõe Value Objects internos (RegistroAcademico, NomeAluno)
- ✅ Adiciona campo calculado (`tentativasRestantes`, `situacao`)
- ✅ Formato amigável para frontend consumir

---

## 6️⃣ Camada Service vs Use Cases

### ❌ Não temos @Service tradicional!

**Por quê?**
- Seguimos **Clean Architecture**
- Usamos **Use Cases** ao invés de Services genéricos
- Cada Use Case tem **uma responsabilidade** (SRP)

### ✅ Estrutura com Use Cases

```java
@Service
public class CriarAlunoUseCase {
    private final AlunoRepository repository;
    
    @Transactional
    public Aluno executar(String nome, String ra) {
        // Validação
        // Criação
        // Persistência
        return repository.salvar(aluno);
    }
}
```

**Vantagens:**
- ✅ **Testável**: Mock apenas o repository necessário
- ✅ **Clara**: Nome do Use Case descreve exatamente o que faz
- ✅ **Reutilizável**: Outro Use Case pode chamar este
- ✅ **SOLID**: Cada classe tem uma única razão para mudar

---

## 7️⃣ Camada Controller

### Anotações Spring MVC

```java
@RestController
@RequestMapping("/api/alunos")
@Tag(name = "Alunos", description = "API de gerenciamento")
public class AlunoController {
    
    @PostMapping
    @Operation(summary = "Criar aluno")
    public ResponseEntity<AlunoResponseDTO> criar(@Valid @RequestBody CriarAlunoRequestDTO request) {
        // ...
    }
}
```

**Responsabilidades:**
- ✅ **Receber requisições** HTTP
- ✅ **Validar entrada** (@Valid)
- ✅ **Delegar para Use Cases**
- ✅ **Mapear para DTOs**
- ✅ **Retornar respostas** HTTP

**NÃO faz:**
- ❌ Lógica de negócio
- ❌ Acesso direto ao repository
- ❌ Validações complexas

---

## 8️⃣ Swagger / OpenAPI

### 📄 Documentação Automática

**Acessos:**
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- JSON: `http://localhost:8080/v3/api-docs`
- YAML: `http://localhost:8080/v3/api-docs.yaml`

### 📸 Gerar PDF do Swagger

**Opção 1: Via Navegador**
1. Abrir `http://localhost:8080/swagger-ui.html`
2. Ctrl + P (Imprimir)
3. Salvar como PDF

**Opção 2: Via curl**
```bash
curl http://localhost:8080/v3/api-docs > api-docs.json
```

---

## 9️⃣ Jenkins Pipeline

### 🚀 Estrutura do Pipeline DEV

```groovy
pipeline {
    agent any
    
    stages {
        stage('Build') {
            steps {
                sh 'mvn clean compile'
            }
        }
        
        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
        
        stage('Quality Gate') {
            steps {
                sh 'mvn jacoco:check'
            }
        }
        
        stage('Docker Build') {
            when {
                expression { jacoco >= 99% }
            }
            steps {
                sh 'docker build -t app:latest .'
            }
        }
    }
}
```

---

## 🔟 Configuração de Quality Gate (99%)

### pom.xml - JaCoCo

```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.11</version>
    <executions>
        <execution>
            <id>jacoco-check</id>
            <goals>
                <goal>check</goal>
            </goals>
            <configuration>
                <rules>
                    <rule>
                        <element>PACKAGE</element>
                        <limits>
                            <limit>
                                <counter>LINE</counter>
                                <value>COVEREDRATIO</value>
                                <minimum>0.99</minimum> <!-- 99% -->
                            </limit>
                        </limits>
                    </rule>
                </rules>
            </configuration>
        </execution>
    </executions>
</plugin>
```

---

## 1️⃣1️⃣ Docker Build Trigger

### Jenkinsfile com Trigger

```groovy
stage('Quality Gate') {
    steps {
        script {
            def coverage = sh(
                script: "mvn jacoco:report | grep 'Total.*%'",
                returnStdout: true
            ).trim()
            
            if (coverage >= 99) {
                build job: 'Image_Docker', wait: false
            }
        }
    }
}
```

---

## 1️⃣2️⃣ Sub-Pipelines

```
Pipeline DEV
├── Pipeline-test-dev (testes)
│   └── Quality Gate check (99%)
│       ├── PASS → Trigger Image_Docker
│       └── FAIL → Stop pipeline
└── Image_Docker (build da imagem)
```

---

## 1️⃣3️⃣ Estratégias de Teste

### @DataJpaTest (Repository)

```java
@DataJpaTest
class AlunoRepositoryTest {
    @Autowired
    private AlunoJpaRepository repository;
    
    @Test
    void deveSalvarAluno() {
        // Teste isolado do repository
    }
}
```

### @MockBean (Use Cases)

```java
@ExtendWith(MockitoExtension.class)
class CriarAlunoUseCaseTest {
    @Mock
    private AlunoRepository repository;
    
    @InjectMocks
    private CriarAlunoUseCase useCase;
    
    @Test
    void deveCriarAluno() {
        // Teste unitário com mock
    }
}
```

### @WebMvcTest (Controller)

```java
@WebMvcTest(AlunoController.class)
class AlunoControllerTest {
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private CriarAlunoUseCase useCase;
    
    @Test
    void deveRetornar201AoCriar() throws Exception {
        mockMvc.perform(post("/api/alunos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isCreated());
    }
}
```

---

**✅ Documentação completa para AC2 DevOps!**
