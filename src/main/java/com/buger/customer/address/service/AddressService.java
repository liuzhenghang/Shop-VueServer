package com.buger.customer.address.service;

import com.buger.customer.address.po.Address;

public interface AddressService {
    Object getMyAddress(int uid);
    Object updateAddAddress(Address address);
    Object setAddressIsDefault(Address address);
    Object removeAddress(Address address);
    void setFirstIsDefault(int uid);
    Object getDefaultAddress(int uid);
}
