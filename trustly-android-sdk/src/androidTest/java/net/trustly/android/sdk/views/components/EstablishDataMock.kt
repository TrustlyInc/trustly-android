package net.trustly.android.sdk.views.components

object EstablishDataMock {

    fun getEstablishDataValues(): MutableMap<String, String> {
        val establishDataValues: MutableMap<String, String> = HashMap()
        establishDataValues["accessId"] = "accessid12345"
        establishDataValues["accessKey"] = "accesskey12345"
        establishDataValues["amount"] = "0.00"
        establishDataValues["merchantId"] = "0123"
        establishDataValues["currency"] = "USD"
        establishDataValues["merchantReference"] = "g:abcdefg-123456"
        establishDataValues["paymentType"] = "Deferred"
        establishDataValues["env"] = "sandbox"
        establishDataValues["description"] = "Description"
        establishDataValues["displayAmount"] = "0.00"
        establishDataValues["minimumBalance"] = "0.00"
        establishDataValues["cancelUrl"] = "/cancelUrl"
        establishDataValues["returnUrl"] = "/returnUrl"
        establishDataValues["globex.lang"] = "en"
        establishDataValues["metadata.lang"] = "en"
        establishDataValues["customer.name"] = "User Name"
        establishDataValues["customer.address.address1"] = "100 Street"
        establishDataValues["customer.address.address2"] = "#100"
        establishDataValues["customer.address.city"] = "Beverly Hills"
        establishDataValues["customer.address.state"] = "CA"
        establishDataValues["customer.address.zip"] = "90000"
        establishDataValues["customer.address.country"] = "US"
        establishDataValues["customer.email"] = "username@email.com"
        establishDataValues["customer.driverLicense.number"] = "124ABC"
        establishDataValues["customer.driverLicense.state"] = "CA"
        establishDataValues["customer.dateOfBirth"] = "00/00/0000"
        establishDataValues["customer.externalId"] = "123456"
        establishDataValues["customer.taxId"] = "1234567890"
        establishDataValues["customer.phone"] = "+1 123456789"
        establishDataValues["customer.vip"] = "12345"
        return establishDataValues
    }

}