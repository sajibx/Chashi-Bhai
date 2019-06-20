package com.example.demo

import com.fasterxml.jackson.databind.util.JSONPObject
import org.apache.catalina.User
import org.apache.catalina.UserDatabase
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.web.bind.annotation.*
import java.awt.Image
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.xml.crypto.Data
import kotlin.collections.ArrayList
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder.json
import java.io.Reader


@Entity
class Users(@Id @GeneratedValue(strategy = GenerationType.AUTO) val id: Long? = null, var fullname: String? = null, var phonenumber: String? = null, var password: String? = null, var address: String? = null, var street_address: String? = null, var city: String? = null, var zipcode: String? = null, var info: String? = null, var state: Int = 0)

@Entity
class Products(@Id @GeneratedValue(strategy = GenerationType.AUTO) var id: Long? = null, var productname: String? = null, var producttype: String? = null, var productdetail: String? = null, var productprice: String? = null, var productseller: Long = 0, var productbuyer: String? = null, var seller_name: String? = null, var seller_number: Long? = null, var ratingGood: Int? = null, var ratingBad: Int? = null, var product_amount: Int? = null, var product_pic: String? = null, var seller_id: Long? = null, var key_data: String? = null)

@Entity
class Products_Extend(@Id @GeneratedValue(strategy = GenerationType.AUTO) var id: Long? = null, var seller_id: Long? = null, var buyer_number: Long? = null, var buyer_quantity: Int? = null, var product_id: Long? = null, var product_name: String? = null)

@Entity
class Product_Type(@Id @GeneratedValue(strategy = GenerationType.AUTO) var id: Long? = null, var product_type_name: String? = null, product_type_code: Int? = null)

class LogingClass(val username: String? = null, val password: String? = null)

interface UsersRepository : JpaRepository<Users, Long> {

    fun getUsersByPhonenumberAndPassword(phonenumber: String?, password: String?): Users
}


interface ProductRepository : JpaRepository<Products, Long> {

    @Query("select  p from Products  p where p.productprice  >?1")
    fun getAllByProductpriceLessThan(productprice: Int?): MutableList<Products>

    @Query("select ps from Products ps where ps.producttype = ?1")
    fun getByKey(key_data: String?): MutableList<Products>

    @Query("select pr from Products pr where pr.producttype = ?1 or pr.productname = ?2")
    fun getByKey2(key_data: String?) : MutableList<Products>

    @Query("select pq from Products pq where pq.productseller = ?1")
    fun getBySeller(productseller: Long): MutableList<Products>

    @Query("select tq from Products tq where tq.id = ?1")
    fun getByRate(id: Long?):MutableList<Products>

}

interface Product_ExtendRepository : JpaRepository<Products_Extend, Long> {
    @Query("select p from Products_Extend p where p.seller_id =?1")
    fun getByExtendSeller(seller_id: Long?): MutableList<Products_Extend>

    @Query("delete from Products_Extend pr where pr.product_id =?1")
    fun getByTendId(product_id: Long): String

    @Query("select pq from Products_Extend pq where pq.product_id = ?1")
    fun getByThat(product_id: Long): MutableList<Products_Extend>

    //@Query("select qt from Products_Extend  qt where Products_Extend.buyer_number =?1 and qt.product_id =?2 ")
    //fun getallProduct(buyer_number: Long, product_id: Long): Products_Extend
}

interface Product_TypeRepository : JpaRepository<Product_Type, Long>
{

}

@RestController
@RequestMapping("/home")
class UserController(val usersRepository: UsersRepository, val productsRepository: ProductRepository, val product_extendRepository: Product_ExtendRepository, val products_typeRepository: Product_TypeRepository) {


    @GetMapping("/Allusers1")
    fun getAll1(): MutableList<Users> {

        var myr = usersRepository.findAll()

        return myr

    }

    @GetMapping("/Allusers2")
    fun getAll2(): Users? {

        var k = usersRepository.findAll()
        //var b:Users? = null

        k.forEach {

            return it

        }
        return null
    }


    class mypro(var sin_id: Long)

    @PostMapping("/ProductClick")
    fun ProductClick(@RequestBody sin_data: mypro): Products? {
        var myt = productsRepository.findById(sin_data.sin_id.toLong())
        var myq = Products()
        myq = myt.get()
        return myq
    }


    class mylog(var sin_number: String, var sin_pass: String)

    @PostMapping("/Login")
    fun Login(@RequestBody sin_data: mylog): Users? {

        var myr = usersRepository.findAll()
        //var myt = Users()

        for (item in myr) {
            if (sin_data.sin_number == item.phonenumber && sin_data.sin_pass == item.password && item.state == 1) {
                return item
            }
        }
        return null
    }





    class rate(var rate_id:Long,var rating_1:Int, var flag:Int)

    @PostMapping("/Rate")
    fun myRate(@RequestBody my_rate:rate):Boolean
    {
        var p = productsRepository.findById(my_rate.rate_id)
        var data = Products()
        if (p.isPresent) {
            data = p.get()

            if (my_rate.flag == 1)
            {
                var t = data.ratingGood
                var s = my_rate.rating_1 + t!!
                data.ratingGood = s
                //data.product_amount = pro_up.up_amount
                productsRepository.save(data)
            }
            else if (my_rate.flag == 2)
            {
                var t = data.ratingBad
                var s = my_rate.rating_1 + t!!
                data.ratingBad = s
                //data.product_amount = pro_up.up_amount
                productsRepository.save(data)
            }

            else
            {
                var t1 = data.ratingBad
                var t2 = data.ratingGood
                var s1 = my_rate.rating_1 + 0
                var s2 = my_rate.rating_1 + 0
                data.ratingBad = s1
                data.ratingGood = s2
                //data.product_amount = pro_up.up_amount
                productsRepository.save(data)
            }
            return true

            //return "save"
        }
        else
            return false

    }


//    class rate1(var rate_id:Long,var rating_1:Int)
//
//    @PostMapping("/Rate1")
//    fun myRate1(@RequestBody my_rate1:rate1):Boolean
//    {
//        var p = productsRepository.findById(my_rate1.rate_id)
//        var data = Products()
//        if (p.isPresent)
//        {
//            if (data.rating!=0)
//            {
//                if (data.rating!! ==100)
//                {
//                    data = p.get()
//                    var t = data.rating
//
//                    data.rating = t
//                    //data.product_amount = pro_up.up_amount
//                    productsRepository.save(data)
//                    return true
//                }
//                else if(data.rating == -100)
//                {
//                    data = p.get()
//                    var t = data.rating
//                    data.rating = t
//                    //data.product_amount = pro_up.up_amount
//                    productsRepository.save(data)
//                    return true
//                }
//            }
//            else
//            {
//                data = p.get()
//                var t = data.rating
//                var s = my_rate1.rating_1 + t!!
//                data.rating = s
//                //data.product_amount = pro_up.up_amount
//                productsRepository.save(data)
//                return true
//            }
//            //return "save"
//            return true
//        }
//        else
//            return false
//
//    }







    class the_opinion(var opinion_number: Long, var opinion_id: Long)

    @PostMapping("/OpinionCheck")
    fun check_opinion(@RequestBody opinion_data: the_opinion): Boolean{
      var data =  product_extendRepository.findAll().filter {
            it.buyer_number == opinion_data.opinion_number
                    && it.product_id == opinion_data.opinion_id
        }
        return (data.isNotEmpty())
    }


    class my_pro(var my_product: Long)

    @PostMapping("/my_product")
    fun my_product(@RequestBody pro_my: my_pro): MutableList<Products>? {
//        var k = productsRepository.findAll()
//        var f = ArrayList<Products>()
//
//        for (i in k)
//        {
//            if (i.id == pro_my.my_product)
//            {
//                f.add(i)
//                return f
//            }
//        }
//        return f
        var t = productsRepository.getBySeller(pro_my.my_product)
        return t

    }


    class update(var up_id: Long? = null, var up_amount: Int? = null)

    @PostMapping("/update")
    fun updater(@RequestBody pro_up: update): String {
        var p = productsRepository.findById(pro_up.up_id!!)
        var data = Products()
        if (p.isPresent) {
            data = p.get()
            data.product_amount = pro_up.up_amount
            productsRepository.save(data)
            return "save"
        }
        return "save"
    }


    class my_prod(var my_product: Long)

    @PostMapping("/sold_product")
    fun sold_product(@RequestBody pro_my: my_prod): MutableList<Products_Extend>? {
//        var k = productsRepository.findAll()
//        var f = ArrayList<Products>()
//
//        for (i in k)
//        {
//            if (i.id == pro_my.my_product)
//            {
//                f.add(i)
//                return f
//            }
//        }
//        return f
        var k = product_extendRepository.getByExtendSeller(pro_my.my_product)
        return k

    }

    @DeleteMapping("/delete_product")
    fun delete_product(@RequestBody product_id: Long): String {
        productsRepository.deleteById(product_id)
        return "hoise"
    }


    class test_data(var search_names: String)

    @PostMapping("/my_search")
    fun my_search(@RequestBody data_test: test_data): MutableList<Products> {
//        var k = productsRepository.findAll()
//
//        for (i in k)
//        {
//            if (i.key_data == data_test.names)
//            {
//                return i
//            }
//        }
//
//        return null

        var p = productsRepository.getByKey(data_test.search_names)
        return p

        //return productsRepository.findAll()
    }


    @PostMapping("/my_search2")
    fun my_search2(@RequestBody data_test: test_data): MutableList<Products> {
//        var k = productsRepository.findAll()
//
//        for (i in k)
//        {
//            if (i.key_data == data_test.names)
//            {
//                return i
//            }
//        }
//
//        return null

        var p = productsRepository.getByKey2(data_test.search_names)
        return p

        //return productsRepository.findAll()
    }


    class bal(var pro_id: Long)

    @PostMapping("/delete_2")
    fun del(@RequestBody mybal: bal): String {
        var p = productsRepository.findById(mybal.pro_id)
        var data = p.get()
        productsRepository.delete(data)
        return "hoise"
    }


    @PostMapping("/deleteType2")
    fun del3(@RequestBody mybal: bal): String {
        var p = products_typeRepository.findById(mybal.pro_id)
        var data = p.get()
        products_typeRepository.delete(data)
        return "hoise"
    }


    class mybal4(var nameType:Long)
    @PostMapping("/deleteType")
    fun deletetype(@RequestBody product_type:mybal4):String
    {

        var p = products_typeRepository.findById(product_type.nameType)
        var data = p.get()
        products_typeRepository.delete(data)
        return "hoise"

    }


    class bal2(var pro_id: Long)

    @PostMapping("/delete_4")
    fun del2(@RequestBody mybal: bal): String {
        var p = product_extendRepository.findById(mybal.pro_id)
        var data = p.get()
        product_extendRepository.delete(data)
        return "hoise"
    }

    class bal4(var pro_id: Long)

    @PostMapping("/delete_6")
    fun del4(@RequestBody mybal: bal): String {
        var t = product_extendRepository.getByThat(mybal.pro_id)
        product_extendRepository.deleteAll(t)
        return "hoise"
    }


    @PostMapping("/LoginNew")
    fun LoginNew(@RequestBody sin_data: mylog): Users? {
        var users = usersRepository.getUsersByPhonenumberAndPassword(sin_data.sin_number, sin_data.sin_pass)

        return users
    }


    class mylog1(var sin_id: Long)

    @PostMapping("/Login1")
    fun Login1(@RequestBody sin_data: mylog1): Users? {

        var myr = usersRepository.findById(sin_data.sin_id.toLong())
        var myt = Users()
        myt = myr.get()
        return myt
    }


    @GetMapping("/Allusers")
    fun getAll(): MutableList<Users> {
        return usersRepository.findAll()
    }


    @GetMapping("/Allproducts")
    fun getAllProduct(): MutableList<Products> {

        var myr = productsRepository.findAll()

        return myr
    }


    data class Data1(val id: Long? = null, var productbuyer: Long? = null, var productdetail: String? = null, var productname: String? = null, var productprice: String? = null, var productseller: Long? = null, var producttype: String? = null, var seller_name: String? = null, var seller_number: Long? = null)


    @GetMapping("/Allproducts1")
    fun getAllProduct2(): MutableList<Products>? {

        var myr = productsRepository.findAll()
        //myr.asReversed()

        return myr.asReversed()
    }

    @GetMapping("/AllProductType")
    fun getAllProductType(): MutableList<Product_Type>? {

        var myr = products_typeRepository.findAll()
        //myr.asReversed()

        return myr.asReversed()
    }


    class search_data(var productnames: String? = null)

    @PostMapping("/SearchProduct")
    fun searchProduct(@RequestBody mydata: String, mydata1: search_data): Users {
        //var myP: Optional<Products> = productsRepository.findById(mydata1.productnames!!)


        val myP: Optional<Users> = usersRepository.findById(mydata.toLong())
        var data = Users()
        if (myP.isPresent) {
            data = myP.get()
            return data
        } else {
            var data = Users()
            return data
        }

    }


    class search_data2(var productnames: MutableList<Users>)

    @PostMapping("/SearchProduct2")
    fun searchProduct2(@RequestBody mydata: String): Users? {
        //var myP: Optional<Products> = productsRepository.findById(mydata1.productnames!!)

        var myq = usersRepository.findAll()
        myq.forEach {
            if (it.info == mydata) {
                return it
            }
        }
        return null
    }


    class search_data4(var productnames: MutableList<Products>)


    var name2: MutableList<Products>? = null

    @PostMapping("/SearchProduct4")
    fun searchProduct4(@RequestBody mydata: String): Products? {
        //var myP: Optional<Products> = productsRepository.findById(mydata1.productnames!!)


        var myq = Products()
        var myr = productsRepository.findAll()

        myr.forEach()
        {
            if (it.producttype == mydata) {
                //myq.keyword1 = it.keyword1
                //myq.productbuyer = it.productbuyer
                //myq.productdetail = it.productdetail
                //myq.productname = it.productname
                //myq.productprice = it.productprice
                //myq.producttype = it.producttype
                //myq.rating = it.rating
                //myq.productseller = it.productseller
                //myq.id = it.id
                //return  myq
                return it
            }
        }

        return null
    }


    @PostMapping("/SearchProduct6")
    fun searchProduct6(@RequestBody mydata: String): Users? {
        //var myP: Optional<Products> = productsRepository.findById(mydata1.productnames!!)


        var myq = Products()
        var myr = usersRepository.findAll()

        for (items in myr) {
            if (items.info == mydata) {
                return items
            }

        }
        return null
    }


    class search_data1(var productid: Long? = null, var userid: Long? = null)

    @PostMapping("/UpdateProducts")
    fun updateProduct(@RequestBody mydata: search_data) {
        //var myP: Optional<Products> = productsRepository.findById(mydata.userid!!)
        //var data = Products()
        //if (myP.isPresent) {
        //       data = myP.get()
        //    data.productbuyer = mydata.userid!!
        //    return productsRepository.save(data)
        //}
        //return data

        //return productsRepository.equals(mydata.productnames==data.productname || mydata.productnames == data.keyword)
    }


    data class datacheck2(var lol_id: Long? = null, var lol_number: Long? = null)

    @PostMapping("/UpdateProducts2")
    fun updateProduct1(@RequestBody data_l: datacheck2): Products {

        var myP = productsRepository.findById(data_l.lol_id!!)
        var data = Products()
        if (myP.isPresent) {
            data = myP.get()
            data.productbuyer = data_l.lol_number.toString()
            return productsRepository.save(data)
        }
        return data

        //return productsRepository.equals(mydata.productnames==data.productname || mydata.productnames == data.keyword)
    }


    data class datacheck(var my_id: Long, var my_number: String)

    @PostMapping("/UpdateProducts1")
    fun updateProduct(@RequestBody mydata2: datacheck): Products {

        var myP = productsRepository.findById(mydata2.my_id)
        var data = Products()
        if (myP.isPresent) {
            data = myP.get()
            data.productbuyer = mydata2.my_number.toString()
            return productsRepository.save(data)
        }
        return data

        //return productsRepository.equals(mydata.productnames==data.productname || mydata.productnames == data.keyword)
    }


    class data_amount(var products_id: Long, var products_amount: Int)


    @PostMapping("/Buy1")
    fun Buy1(@RequestBody mydata2: data_amount): String {

        var myP = productsRepository.findById(mydata2.products_id)
        var data = Products()
        if (myP.isPresent) {
            data = myP.get()
            data.product_amount = mydata2.products_amount
            productsRepository.save(data)
        }
        return mydata2.products_amount.toString()

        //return productsRepository.equals(mydata.productnames==data.productname || mydata.productnames == data.keyword)
    }

    data class buy_data(var buy_product_id: Long?, var buy_seller_id: Long?, var buy_buyer_number: Long?, var buy_buyer_amount: Int?, var the_name: String?)

    @PostMapping("/Buy")
    fun Buy(@RequestBody buy_data_buy: buy_data): String {
        //return buy_data_buy.buy_buyer_number.toString()
        var mydata = Products_Extend()

        mydata.seller_id = buy_data_buy.buy_seller_id
        mydata.product_id = buy_data_buy.buy_product_id
        mydata.buyer_quantity = 1//buy_data_buy.buy_buyer_amount
        mydata.buyer_number = buy_data_buy.buy_buyer_number
        mydata.product_name = buy_data_buy.the_name

        product_extendRepository.save(mydata)

        return "hoise"
    }


    class crew(var prodid: Long? = null)

    @PostMapping("/removeProduct")
    fun removeProduct(@RequestBody dataa: crew): Products {
        var myP: Optional<Products> = productsRepository.findById(dataa.prodid!!)
        var data = Products()
        if (myP.isPresent) {
            data = myP.get()
            //data.productbuyer = 0
            //data.rating = null
            return productsRepository.save(data)
        }
        return data
    }


    class crews(var proid: Long? = null)

    @PostMapping("/deleteProduct")
    fun deleteProduct(@RequestBody datas: crews): Products {
        var myP: Optional<Products> = productsRepository.findById(datas.proid!!)
        var data = Products()
        if (myP.isPresent) {
            data = myP.get()
            //data.productbuyer = 0
            data.productdetail = null
            data.productname = null
            data.productprice = null
            data.productseller = 0
            data.producttype = null
            //data.rating = null
            return productsRepository.save(data)
        }
        return data
    }


    class rat(var proid: Long? = null, var ratings: String? = null)

    @PostMapping("/rateProduct")
    fun productRate(@RequestBody info: rat): Products {
        var myP: Optional<Products> = productsRepository.findById(info.proid!!)
        var data = Products()
        if (myP.isPresent) {
            data = myP.get()
            //data.rating = info.ratings!!
            return productsRepository.save(data)
        }
        return data
    }


    @PostMapping("/saveuser")
    fun saveUsers(@RequestBody users: Users): String {
        print(users.fullname)
        usersRepository.save(users)
        return " Hoise "
    }

    @PostMapping("/saveType")
    fun savetype(@RequestBody product_type: Product_Type): String {
        print(product_type.product_type_name)
        products_typeRepository.save(product_type)
        return "hoise"
    }


    @PostMapping("/saveproduct")
    fun saveProduct(@RequestBody product: Products): String {
        print(product.productname)
        productsRepository.save(product)
        return " Hoise "
    }

    @PostMapping("/sellproduct")
    fun saveUsers(@RequestBody products: Products): String {
        productsRepository.save(products)
        return " Hoise "
    }

    /*@PostMapping("/Lgin")
    fun lohin(@RequestBody products: LogingClass): Users {
        return usersRepository.findinLog(products)

    }*/

    @PostMapping("/updateuser")
    fun updateUsers(@RequestBody users: Users): String {
        print(users.fullname)
        usersRepository.save(users)
        return " Hoise "
    }

}