/*
 * MIT License
 *
 * Copyright (c) 2026 Fabricio Batista Narcizo
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package dk.itu.moapd.lazylist.domain.sampledata

import com.github.javafaker.Faker
import dk.itu.moapd.lazylist.domain.model.DummyModel
import java.util.Random

/**
 * A factory to create a list of dummy data using the JavaFaker library.
 */
object DummyDataFactory {

    /**
     * A seed value for the random generator to ensure reproducibility.
     */
    private const val SEED = 42L

    /**
     * Creates a list of [DummyModel] objects with fake data.
     *
     * @param count The number of dummy data items to create. Default is 50.
     *
     * @return A list of [DummyModel] objects.
     */
    fun create(count: Int = 50): List<DummyModel> {
        val faker = Faker(Random(SEED))
        return (1..count).map { index ->
            val address = faker.address()
            DummyModel(
                cityName = address.cityName(),
                zipCode = address.zipCode(),
                country = address.country(),
                description = faker.lorem().paragraph(),
                url = "https://picsum.photos/seed/$index/400/194"
            )
        }
    }
}
