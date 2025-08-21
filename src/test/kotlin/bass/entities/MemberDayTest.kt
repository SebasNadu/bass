package bass.entities

import bass.exception.DayNameAlreadyExistsException
import bass.exception.DaysSizeAlreadyMaximumException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class MemberDayTest {
    @Test
    fun `init - should NOT have more than 2 days`() {
        // given
        val dow1 =
            DayEntity(
                dayName = DayEntity.DayOfWeek.MONDAY,
            )
        val dow2 =
            DayEntity(
                dayName = DayEntity.DayOfWeek.FRIDAY,
            )
        val dow3 =
            DayEntity(
                dayName = DayEntity.DayOfWeek.SATURDAY,
            )
        val newSet = mutableSetOf(dow1, dow2, dow3)

        // when
        // then
        assertThrows<IllegalArgumentException> {
            MemberEntity(
                name = "New member",
                email = "newmember@gmail.com",
                password = "lalala",
                days = newSet,
            )
        }
    }

    @Test
    fun `addDay() - should the method sync collection`() {
        // given
        val newMember =
            MemberEntity(
                name = "New member",
                email = "newmember@gmail.com",
                password = "lalala",
            )

        val dow1 =
            DayEntity(
                dayName = DayEntity.DayOfWeek.MONDAY,
            )

        // when
        newMember.addDay(dow1)

        // then
        assertThat(newMember.days.first()).isEqualTo(dow1)
        assertThat(dow1.member).isEqualTo(newMember)
    }

    @Test
    fun `addDay() - should throw exception when add more than two days of day of week`() {
        // given
        val newMember =
            MemberEntity(
                name = "New member",
                email = "newmember@gmail.com",
                password = "lalala",
            )

        val dow1 =
            DayEntity(
                dayName = DayEntity.DayOfWeek.MONDAY,
            )
        val dow2 =
            DayEntity(
                dayName = DayEntity.DayOfWeek.FRIDAY,
            )
        val dow3 =
            DayEntity(
                dayName = DayEntity.DayOfWeek.SATURDAY,
            )

        // when
        newMember.addDay(dow1)
        newMember.addDay(dow2)

        // then
        assertThrows<DaysSizeAlreadyMaximumException> { newMember.addDay(dow3) }
    }

    @Test
    fun `addDay() - should throw exception when add the same name of day of week that already added`() {
        // given
        val newMember =
            MemberEntity(
                name = "New member",
                email = "newmember@gmail.com",
                password = "lalala",
            )

        val dow1 =
            DayEntity(
                dayName = DayEntity.DayOfWeek.MONDAY,
            )
        val dow2 =
            DayEntity(
                dayName = DayEntity.DayOfWeek.MONDAY,
            )
        // when
        newMember.addDay(dow1)

        // then
        assertThrows<DayNameAlreadyExistsException> { newMember.addDay(dow2) }
    }

    @Test
    fun `removeDay() - should the method sync collection`() {
        // given
        val newMember =
            MemberEntity(
                name = "New member",
                email = "newmember@gmail.com",
                password = "lalala",
            )

        val dow1 =
            DayEntity(
                dayName = DayEntity.DayOfWeek.MONDAY,
            )
        newMember.addDay(dow1)

        // when
        newMember.removeDay(dow1)

        // then
        assertThat(newMember.days).isEmpty()
        assertThat(dow1.member).isNull()
    }

    @Test
    fun `removeDay() - assert when days is empty`() {
        // given
        val newMember =
            MemberEntity(
                name = "New member",
                email = "newmember@gmail.com",
                password = "lalala",
            )

        val dow1 =
            DayEntity(
                dayName = DayEntity.DayOfWeek.MONDAY,
            )

        // when
        // then
        assertThrows<AssertionError> { newMember.removeDay(dow1) }
    }

    @Test
    fun `removeDay() - assert when days do not have the name of the day`() {
        // given
        val newMember =
            MemberEntity(
                name = "New member",
                email = "newmember@gmail.com",
                password = "lalala",
            )

        val dow1 =
            DayEntity(
                dayName = DayEntity.DayOfWeek.MONDAY,
            )
        val dow2 =
            DayEntity(
                dayName = DayEntity.DayOfWeek.SATURDAY,
            )

        // when
        newMember.addDay(dow1)

        // then
        assertThrows<AssertionError> { newMember.removeDay(dow2) }
    }
}
