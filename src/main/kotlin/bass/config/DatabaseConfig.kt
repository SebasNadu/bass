package bass.config

import bass.entities.CartItemEntity
import bass.entities.MealEntity
import bass.entities.MemberEntity
import bass.entities.TagEntity
import bass.repositories.CartItemRepository
import bass.repositories.MealRepository
import bass.repositories.MemberRepository
import bass.repositories.TagRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import java.time.Instant

@Configuration
@Profile("dev")
class DatabaseConfig(
    private val memberRepository: MemberRepository,
    private val mealRepository: MealRepository,
    private val cartItemRepository: CartItemRepository,
    private val tagRepository: TagRepository,
) {
    @Bean
    fun databaseInitializer(): CommandLineRunner =
        CommandLineRunner {
            val tags =
                listOf(
                    TagEntity(name = "healthy"),
                    TagEntity(name = "vegan"),
                    TagEntity(name = "high-Protein"),
                    TagEntity(name = "salad"),
                    TagEntity(name = "bowl"),
                )
            val savedTags = tagRepository.saveAll(tags)

            val meals = listOf(
                MealEntity(
                    name = "Salmon Poke Bowl",
                    price = 14.50.toBigDecimal(),
                    quantity = 50,
                    imageUrl = "https://images.unsplash.com/photo-1604259596863-57153177d40b?q=80&w=687&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                    description = "Sushi rice, raw salmon, avocado, edamame, and mango."
                ).apply {
                    addTag(savedTags[0]); addTag(savedTags[4])
                },
                MealEntity(
                    name = "Quinoa Buddha Bowl",
                    price = 12.50.toBigDecimal(),
                    quantity = 50,
                    imageUrl = "https://images.unsplash.com/photo-1679279726937-122c49626802?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                    description = "Quinoa, roasted chickpeas, sweet potato, and kale."
                ).apply {
                    addTag(savedTags[0]); addTag(savedTags[4])
                },
                MealEntity(
                    name = "Greek Salad",
                    price = 10.50.toBigDecimal(),
                    quantity = 50,
                    imageUrl = "https://images.unsplash.com/photo-1599021419847-d8a7a6aba5b4?q=80&w=979&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                    description = "Feta cheese, olives, cucumber, tomatoes, and red onion."
                ).apply {
                    addTag(savedTags[0]); addTag(savedTags[1]); addTag(savedTags[3])
                },
                MealEntity(
                    name = "Lentil Salad with Goat Cheese",
                    price = 11.00.toBigDecimal(),
                    quantity = 50,
                    imageUrl = "https://plus.unsplash.com/premium_photo-1699881082655-ce3f5590b380?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                    description = "Brown lentils, goat cheese, walnuts, and balsamic vinaigrette."
                ).apply {
                    addTag(savedTags[2]); addTag(savedTags[1])
                },
                MealEntity(
                    name = "Chicken Caesar Salad",
                    price = 12.00.toBigDecimal(),
                    quantity = 50,
                    imageUrl = "https://images.unsplash.com/photo-1580013759032-c96505e24c1f?q=80&w=909&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                    description = "Grilled chicken, romaine lettuce, parmesan, and croutons."
                ).apply {
                    addTag(savedTags[4]); addTag(savedTags[1])
                },
                MealEntity(
                    name = "Vegan Tofu Scramble Bowl",
                    price = 11.50.toBigDecimal(),
                    quantity = 50,
                    imageUrl = "https://images.unsplash.com/photo-1631255325918-1ca2886508a5?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                    description = "Turmeric-spiced tofu with black beans and avocado."
                ).apply {
                    addTag(savedTags[3])
                },
                MealEntity(
                    name = "Teriyaki Chicken Bowl",
                    price = 13.00.toBigDecimal(),
                    quantity = 50,
                    imageUrl = "https://images.unsplash.com/photo-1706703200723-822e740c7e6b?q=80&w=735&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                    description = "Grilled chicken in teriyaki sauce with rice and broccoli."
                ).apply {
                    addTag(savedTags[2]); addTag(savedTags[3])
                },
                MealEntity(
                    name = "Halloumi & Couscous Salad",
                    price = 12.50.toBigDecimal(),
                    quantity = 50,
                    imageUrl = "https://images.unsplash.com/photo-1673960802455-ec189a6207e5?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                    description = "Grilled halloumi, couscous, mint, and pomegranate seeds."
                ).apply {
                    addTag(savedTags[2]); addTag(savedTags[1]); addTag(savedTags[3])
                },
                
                MealEntity(
                    name = "Avocado Toast with Poached Egg",
                    price = 9.00.toBigDecimal(),
                    quantity = 50,
                    imageUrl = "https://images.unsplash.com/photo-1504674900247-0877df9cc836?q=80&w=1080",
                    description = "Whole grain toast topped with smashed avocado and a poached egg."
                ).apply {
                    addTag(savedTags[0]); addTag(savedTags[3])
                },
                MealEntity(
                    name = "Chickpea & Spinach Curry",
                    price = 13.00.toBigDecimal(),
                    quantity = 50,
                    imageUrl = "https://images.unsplash.com/photo-1604908177521-9c7c07b3c040?q=80&w=1080",
                    description = "Creamy chickpea curry with spinach served over basmati rice."
                ).apply {
                    addTag(savedTags[1])
                },
                MealEntity(
                    name = "Grilled Steak with Asparagus",
                    price = 18.50.toBigDecimal(),
                    quantity = 50,
                    imageUrl = "https://images.unsplash.com/photo-1562967916-eb82221dfb7e?q=80&w=1080",
                    description = "Juicy grilled steak with roasted asparagus and garlic butter."
                ).apply {
                    addTag(savedTags[2])
                },
                MealEntity(
                    name = "Cauliflower Rice Stir Fry",
                    price = 11.50.toBigDecimal(),
                    quantity = 50,
                    imageUrl = "https://images.unsplash.com/photo-1551218808-94e220e084d2?q=80&w=1080",
                    description = "Low-carb stir fry with cauliflower rice, mixed vegetables, and soy sauce."
                ).apply {
                    addTag(savedTags[0]); addTag(savedTags[1])
                },
                MealEntity(
                    name = "Zucchini Noodles with Pesto",
                    price = 12.00.toBigDecimal(),
                    quantity = 50,
                    imageUrl = "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?q=80&w=1080",
                    description = "Fresh zucchini noodles tossed in basil pesto and cherry tomatoes."
                ).apply {
                    addTag(savedTags[0]); addTag(savedTags[1])
                },
                MealEntity(
                    name = "Turkey Meatballs with Tomato Sauce",
                    price = 14.00.toBigDecimal(),
                    quantity = 50,
                    imageUrl = "https://images.unsplash.com/photo-1601050690686-9a798fe94f08?q=80&w=1080",
                    description = "Lean turkey meatballs served with homemade tomato sauce."
                ).apply {
                    addTag(savedTags[2])
                },
                MealEntity(
                    name = "Miso Soup with Tofu",
                    price = 8.50.toBigDecimal(),
                    quantity = 50,
                    imageUrl = "https://images.unsplash.com/photo-1505253210343-989b6fdfae4b?q=80&w=1080",
                    description = "Traditional Japanese miso soup with tofu and seaweed."
                ).apply {
                    addTag(savedTags[0]); addTag(savedTags[1])
                },
                MealEntity(
                    name = "Sweet Potato and Black Bean Chili",
                    price = 12.50.toBigDecimal(),
                    quantity = 50,
                    imageUrl = "https://images.unsplash.com/photo-1504674900247-0877df9cc836?q=80&w=1080",
                    description = "Hearty chili with sweet potatoes, black beans, and spices."
                ).apply {
                    addTag(savedTags[0]); addTag(savedTags[1])
                },
                MealEntity(
                    name = "Grilled Shrimp Tacos",
                    price = 15.00.toBigDecimal(),
                    quantity = 50,
                    imageUrl = "https://images.unsplash.com/photo-1552332386-f8dd00dc0ab7?q=80&w=1080",
                    description = "Spicy grilled shrimp tacos with fresh salsa and avocado."
                ).apply {
                    addTag(savedTags[4])
                },
                MealEntity(
                    name = "Eggplant Parmesan",
                    price = 13.50.toBigDecimal(),
                    quantity = 50,
                    imageUrl = "https://images.unsplash.com/photo-1604908177521-9c7c07b3c040?q=80&w=1080",
                    description = "Baked eggplant layered with marinara and mozzarella cheese."
                ).apply {
                    addTag(savedTags[3])
                },
                MealEntity(
                    name = "Thai Green Curry with Vegetables",
                    price = 13.00.toBigDecimal(),
                    quantity = 50,
                    imageUrl = "https://images.unsplash.com/photo-1562967916-eb82221dfb7e?q=80&w=1080",
                    description = "Spicy green curry with mixed vegetables and coconut milk."
                ).apply {
                    addTag(savedTags[1]); addTag(savedTags[0])
                },
                MealEntity(
                    name = "Beef & Broccoli Stir Fry",
                    price = 16.00.toBigDecimal(),
                    quantity = 50,
                    imageUrl = "https://images.unsplash.com/photo-1551218808-94e220e084d2?q=80&w=1080",
                    description = "Tender beef stir-fried with broccoli and soy sauce."
                ).apply {
                    addTag(savedTags[2])
                },
                MealEntity(
                    name = "Spinach & Mushroom Omelette",
                    price = 10.00.toBigDecimal(),
                    quantity = 50,
                    imageUrl = "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?q=80&w=1080",
                    description = "Fluffy omelette with sautéed spinach and mushrooms."
                ).apply {
                    addTag(savedTags[0])
                },
                MealEntity(
                    name = "Grilled Vegetable Panini",
                    price = 11.00.toBigDecimal(),
                    quantity = 50,
                    imageUrl = "https://images.unsplash.com/photo-1601050690686-9a798fe94f08?q=80&w=1080",
                    description = "Panini with grilled zucchini, peppers, and mozzarella."
                ).apply {
                    addTag(savedTags[1]); addTag(savedTags[3])
                }
            )

            val savedMeals = mealRepository.saveAll(meals)

            val admin =
                MemberEntity(
                    name = "sebas",
                    email = "sebas@sebas.com",
                    password = "123456",
                    role = MemberEntity.Role.ADMIN,
                )

            val customers =
                (1..10).map { i ->
                    MemberEntity(
                        name = "User $i",
                        email = "user$i@example.com",
                        password = "pass",
                        role = MemberEntity.Role.CUSTOMER,
                    )
                }

            val savedMembers = memberRepository.saveAll(listOf(admin) + customers)

            val cartItems =
                listOf(
                    CartItemEntity(
                        member = savedMembers[1],
                        meal = savedMeals[0],
                        quantity = 1,
                        addedAt = Instant.now(),
                    ),
                    CartItemEntity(
                        member = savedMembers[1],
                        meal = savedMeals[1],
                        quantity = 2,
                        addedAt = Instant.now(),
                    ),
                )

            cartItemRepository.saveAll(cartItems)
        }
}
