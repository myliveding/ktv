package com.st.mapper.wxtour;

import com.st.javabean.pojo.wxtour.TBUserTransfer;

public interface TBUserTransferMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_user_transfer
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(String openid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_user_transfer
     *
     * @mbggenerated
     */
    int insert(TBUserTransfer record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_user_transfer
     *
     * @mbggenerated
     */
    int insertSelective(TBUserTransfer record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_user_transfer
     *
     * @mbggenerated
     */
    TBUserTransfer selectByPrimaryKey(String openid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_user_transfer
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(TBUserTransfer record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_user_transfer
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(TBUserTransfer record);
}