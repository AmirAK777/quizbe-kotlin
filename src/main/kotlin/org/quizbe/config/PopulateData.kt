package org.quizbe.config

import org.quizbe.dto.*
import org.quizbe.exception.UserNotFoundException
import org.quizbe.model.Rating
import org.quizbe.model.Role
import org.quizbe.model.User
import org.quizbe.service.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.sql.SQLIntegrityConstraintViolationException
import java.time.LocalDateTime

@Order(value = 1)
@Component
class PopulateData @Autowired constructor(
        private val userService: UserService,
        private val roleService: RoleService,
        private val topicService: TopicService,
        private val ratingService: RatingService,
        private val questionService: QuestionService
) : ApplicationRunner {

    var logger = LoggerFactory.getLogger(PopulateData::class.java)!!

    private lateinit var admin: User
    private lateinit var teacher: User
    private lateinit var eleve: User

    /**
     * Injecte des données initiales si la base de données est "vide"
     */
    @Throws(Exception::class)
    override fun run(args: ApplicationArguments) {
        if (roleService.countRole() > 0) {
            return
        }
        roleService.saveRole(Role("USER"))
        roleService.saveRole(Role("TEACHER"))
        roleService.saveRole(Role("ADMIN"))
        createUsers()
        addTopics()
        addQuestions()
        addRatings()
    }

    private fun addRatings() {
        val question1 = questionService.findById(1)
        var rating = Rating(null, "La vie est belle", 5, LocalDateTime.now(), question1, teacher)
        ratingService.save(rating)
        rating = Rating(null, "La vie, cette inconnue", 3, LocalDateTime.now(), question1, eleve)
        ratingService.save(rating)
    }

    private fun addTopics() {
        var topicDto = TopicDto("Topic1")

        topicDto.creatorUsername = teacher.username
        val scopeDtos: MutableList<ScopeDto> = ArrayList()
        scopeDtos.add(ScopeDto("scope1"))
        scopeDtos.add(ScopeDto("scope2"))
        topicDto.setScopesDtos(scopeDtos)
        topicService.saveTopicFromTopicDto(topicDto, null)
        topicDto = TopicDto()
        topicDto.name = "Topic2"
        topicDto.creatorUsername = teacher.username
        scopeDtos.add(ScopeDto("scope3"))
        topicDto.setScopesDtos(scopeDtos)
        topicService.saveTopicFromTopicDto(topicDto, null)

        // users subscribe to topic1
        val topic = topicService.findTopicById(1L).get()
//        admin!!.subscribedTopics.add(topic)
        topic.addSubscribedr(admin)
        topic.addSubscribedr(teacher)
        topic.addSubscribedr(eleve)

        topicService.save(topic)

    }

    private fun addQuestions() {
        val topic = topicService.getTopicById(1L).get()

        val responseDtos = mutableListOf<ResponseDto>()
        responseDtos.add(ResponseDto(null, "_42_", "feedback proposition 1", 1))
        responseDtos.add(ResponseDto(null, "proposition 2", "feedback proposition 2", -1))
        responseDtos.add(ResponseDto(null, "proposition 3", "feedback proposition 3", -2))
        val questionDto = QuestionDto(null, topic, 1L, admin.username)
        questionDto.name = "Question1"
        questionDto.sentence = "Answer to the Ultimate Question of Life, the Universe, and Everything"
        questionDto.responseDtos = responseDtos
        questionService.saveQuestionFromQuestionDto(questionDto)
        val q1 = QuestionDto(null,topic,2L,admin.username)
        q1.name = "QuestionTest"
        q1.sentence = "Answer to the Ultimate Question of Life, the Universe, and Everything"
        q1.responseDtos = responseDtos
        questionService.saveQuestionFromQuestionDto(q1)
        val q2 = QuestionDto(null,topic,1L,admin.username)
        q2.name = "Question2"
        q2.sentence = "Answer to the Ultimate Question of Life, the Universe, and Everything"
        q2.responseDtos = responseDtos
        questionService.saveQuestionFromQuestionDto(q2)
        val q3 = QuestionDto(null,topic,2L,admin.username)
        q3.name = "Question3"
        q3.sentence = "Answer to the Ultimate Question of Life, the Universe, and Everything"
        q3.responseDtos = responseDtos
        questionService.saveQuestionFromQuestionDto(q3)
        val q4 = QuestionDto(null,topic,1L,admin.username)
        q4.name = "Question4"
        q4.sentence = "Answer to the Ultimate Question of Life, the Universe, and Everything"
        q4.responseDtos = responseDtos
        questionService.saveQuestionFromQuestionDto(q4)
        val q5 = QuestionDto(null,topic,2L,admin.username)
        q5.name = "Question5"
        q5.sentence = "Answer to the Ultimate Question of Life, the Universe, and Everything"
        q5.responseDtos = responseDtos
        questionService.saveQuestionFromQuestionDto(q5)
    }

    @Throws(SQLIntegrityConstraintViolationException::class)
    private fun createUsers() {
        val userAdmin = userService.findById(1).orElse(null)
        if (userAdmin == null) {
            val roles: MutableSet<String> = HashSet()
            roles.add("USER")
            roles.add("TEACHER")
            roles.add("ADMIN")
            val adminDto = UserDto("admin", "admin@admin.org", "adminadmin", roles)
            logger.info("userAdmin : $adminDto")
            admin = userService.saveUserFromUserDto(adminDto)
            roles.remove("ADMIN")
            val teacherDto = UserDto("prof", "prof@prof.org", "profprof", roles)
            logger.info("userTeacher : $teacherDto")
            teacher = userService.saveUserFromUserDto(teacherDto)

            roles.remove("TEACHER")
            val eleveDto = UserDto("eleve", "eleve@eleve.org", "eleveeleve", roles)
            logger.info("userEleve : $eleveDto")
            eleve = userService.saveUserFromUserDto(eleveDto)
        }
        this.admin = userService.findByUsername("admin") ?: throw UserNotFoundException("admin not found")
        this.teacher = userService.findByUsername("prof") ?: throw UserNotFoundException("prof not found")
        this.eleve = userService.findByUsername("eleve") ?: throw UserNotFoundException("eleve not found")
    }
}