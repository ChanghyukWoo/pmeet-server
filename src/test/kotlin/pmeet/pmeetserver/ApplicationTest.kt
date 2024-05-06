package pmeet.pmeetserver

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.context.ApplicationContext
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [PmeetServerApplication::class])
class ApplicationTest {

  @Autowired
  private lateinit var applicationContext: ApplicationContext

  @Test
  @Throws(Exception::class)
  fun contextLoads() {
    applicationContext.beanDefinitionNames.forEach { bean ->
      println("bean : $bean")
    }
  }
}
