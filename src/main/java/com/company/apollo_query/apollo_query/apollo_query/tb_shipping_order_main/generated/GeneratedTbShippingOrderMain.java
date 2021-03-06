package com.company.apollo_query.apollo_query.apollo_query.tb_shipping_order_main.generated;

import com.company.apollo_query.apollo_query.apollo_query.tb_shipping_order_main.TbShippingOrderMain;
import com.speedment.runtime.config.identifier.ColumnIdentifier;
import com.speedment.runtime.config.identifier.TableIdentifier;
import com.speedment.runtime.field.ByteField;
import com.speedment.runtime.field.ComparableField;
import com.speedment.runtime.field.IntField;
import com.speedment.runtime.field.LongField;
import com.speedment.runtime.field.StringField;
import com.speedment.runtime.typemapper.TypeMapper;
import java.sql.Timestamp;
import javax.annotation.Generated;

/**
 * The generated base for the {@link
 * com.company.apollo_query.apollo_query.apollo_query.tb_shipping_order_main.TbShippingOrderMain}-interface
 * representing entities of the {@code tb_shipping_order_main}-table in the
 * database.
 * <p>
 * This file has been automatically generated by Speedment. Any changes made to
 * it will be overwritten.
 * 
 * @author Speedment
 */
@Generated("Speedment")
public interface GeneratedTbShippingOrderMain {
    
    /**
     * This Field corresponds to the {@link TbShippingOrderMain} field that can
     * be obtained using the {@link TbShippingOrderMain#getId()} method.
     */
    final LongField<TbShippingOrderMain, Long> ID = LongField.create(
        Identifier.ID,
        TbShippingOrderMain::getId,
        TbShippingOrderMain::setId,
        TypeMapper.primitive(), 
        true
    );
    /**
     * This Field corresponds to the {@link TbShippingOrderMain} field that can
     * be obtained using the {@link TbShippingOrderMain#getTrackingId()} method.
     */
    final LongField<TbShippingOrderMain, Long> TRACKING_ID = LongField.create(
        Identifier.TRACKING_ID,
        TbShippingOrderMain::getTrackingId,
        TbShippingOrderMain::setTrackingId,
        TypeMapper.primitive(), 
        true
    );
    /**
     * This Field corresponds to the {@link TbShippingOrderMain} field that can
     * be obtained using the {@link TbShippingOrderMain#getSourceId()} method.
     */
    final IntField<TbShippingOrderMain, Integer> SOURCE_ID = IntField.create(
        Identifier.SOURCE_ID,
        TbShippingOrderMain::getSourceId,
        TbShippingOrderMain::setSourceId,
        TypeMapper.primitive(), 
        false
    );
    /**
     * This Field corresponds to the {@link TbShippingOrderMain} field that can
     * be obtained using the {@link TbShippingOrderMain#getPlatformTrackingId()}
     * method.
     */
    final StringField<TbShippingOrderMain, String> PLATFORM_TRACKING_ID = StringField.create(
        Identifier.PLATFORM_TRACKING_ID,
        TbShippingOrderMain::getPlatformTrackingId,
        TbShippingOrderMain::setPlatformTrackingId,
        TypeMapper.identity(), 
        false
    );
    /**
     * This Field corresponds to the {@link TbShippingOrderMain} field that can
     * be obtained using the {@link TbShippingOrderMain#getRetailerId()} method.
     */
    final LongField<TbShippingOrderMain, Long> RETAILER_ID = LongField.create(
        Identifier.RETAILER_ID,
        TbShippingOrderMain::getRetailerId,
        TbShippingOrderMain::setRetailerId,
        TypeMapper.primitive(), 
        false
    );
    /**
     * This Field corresponds to the {@link TbShippingOrderMain} field that can
     * be obtained using the {@link TbShippingOrderMain#getShippingOption()}
     * method.
     */
    final ByteField<TbShippingOrderMain, Byte> SHIPPING_OPTION = ByteField.create(
        Identifier.SHIPPING_OPTION,
        TbShippingOrderMain::getShippingOption,
        TbShippingOrderMain::setShippingOption,
        TypeMapper.primitive(), 
        false
    );
    /**
     * This Field corresponds to the {@link TbShippingOrderMain} field that can
     * be obtained using the {@link TbShippingOrderMain#getGridId()} method.
     */
    final LongField<TbShippingOrderMain, Long> GRID_ID = LongField.create(
        Identifier.GRID_ID,
        TbShippingOrderMain::getGridId,
        TbShippingOrderMain::setGridId,
        TypeMapper.primitive(), 
        false
    );
    /**
     * This Field corresponds to the {@link TbShippingOrderMain} field that can
     * be obtained using the {@link TbShippingOrderMain#getCarrierId()} method.
     */
    final IntField<TbShippingOrderMain, Integer> CARRIER_ID = IntField.create(
        Identifier.CARRIER_ID,
        TbShippingOrderMain::getCarrierId,
        TbShippingOrderMain::setCarrierId,
        TypeMapper.primitive(), 
        false
    );
    /**
     * This Field corresponds to the {@link TbShippingOrderMain} field that can
     * be obtained using the {@link TbShippingOrderMain#getAgentId()} method.
     */
    final IntField<TbShippingOrderMain, Integer> AGENT_ID = IntField.create(
        Identifier.AGENT_ID,
        TbShippingOrderMain::getAgentId,
        TbShippingOrderMain::setAgentId,
        TypeMapper.primitive(), 
        false
    );
    /**
     * This Field corresponds to the {@link TbShippingOrderMain} field that can
     * be obtained using the {@link TbShippingOrderMain#getStationId()} method.
     */
    final IntField<TbShippingOrderMain, Integer> STATION_ID = IntField.create(
        Identifier.STATION_ID,
        TbShippingOrderMain::getStationId,
        TbShippingOrderMain::setStationId,
        TypeMapper.primitive(), 
        false
    );
    /**
     * This Field corresponds to the {@link TbShippingOrderMain} field that can
     * be obtained using the {@link TbShippingOrderMain#getCarrierDriverId()}
     * method.
     */
    final StringField<TbShippingOrderMain, String> CARRIER_DRIVER_ID = StringField.create(
        Identifier.CARRIER_DRIVER_ID,
        TbShippingOrderMain::getCarrierDriverId,
        TbShippingOrderMain::setCarrierDriverId,
        TypeMapper.identity(), 
        false
    );
    /**
     * This Field corresponds to the {@link TbShippingOrderMain} field that can
     * be obtained using the {@link TbShippingOrderMain#getShippingState()}
     * method.
     */
    final ByteField<TbShippingOrderMain, Byte> SHIPPING_STATE = ByteField.create(
        Identifier.SHIPPING_STATE,
        TbShippingOrderMain::getShippingState,
        TbShippingOrderMain::setShippingState,
        TypeMapper.primitive(), 
        false
    );
    /**
     * This Field corresponds to the {@link TbShippingOrderMain} field that can
     * be obtained using the {@link TbShippingOrderMain#getShippingReasonCode()}
     * method.
     */
    final ByteField<TbShippingOrderMain, Byte> SHIPPING_REASON_CODE = ByteField.create(
        Identifier.SHIPPING_REASON_CODE,
        TbShippingOrderMain::getShippingReasonCode,
        TbShippingOrderMain::setShippingReasonCode,
        TypeMapper.primitive(), 
        false
    );
    /**
     * This Field corresponds to the {@link TbShippingOrderMain} field that can
     * be obtained using the {@link TbShippingOrderMain#getRemarkCode()} method.
     */
    final StringField<TbShippingOrderMain, String> REMARK_CODE = StringField.create(
        Identifier.REMARK_CODE,
        TbShippingOrderMain::getRemarkCode,
        TbShippingOrderMain::setRemarkCode,
        TypeMapper.identity(), 
        false
    );
    /**
     * This Field corresponds to the {@link TbShippingOrderMain} field that can
     * be obtained using the {@link TbShippingOrderMain#getCityid()} method.
     */
    final IntField<TbShippingOrderMain, Integer> CITYID = IntField.create(
        Identifier.CITYID,
        TbShippingOrderMain::getCityid,
        TbShippingOrderMain::setCityid,
        TypeMapper.primitive(), 
        false
    );
    /**
     * This Field corresponds to the {@link TbShippingOrderMain} field that can
     * be obtained using the {@link TbShippingOrderMain#getAcceptAt()} method.
     */
    final ComparableField<TbShippingOrderMain, Timestamp, Timestamp> ACCEPT_AT = ComparableField.create(
        Identifier.ACCEPT_AT,
        TbShippingOrderMain::getAcceptAt,
        TbShippingOrderMain::setAcceptAt,
        TypeMapper.identity(), 
        false
    );
    /**
     * This Field corresponds to the {@link TbShippingOrderMain} field that can
     * be obtained using the {@link TbShippingOrderMain#getCompleteAt()} method.
     */
    final ComparableField<TbShippingOrderMain, Timestamp, Timestamp> COMPLETE_AT = ComparableField.create(
        Identifier.COMPLETE_AT,
        TbShippingOrderMain::getCompleteAt,
        TbShippingOrderMain::setCompleteAt,
        TypeMapper.identity(), 
        false
    );
    /**
     * This Field corresponds to the {@link TbShippingOrderMain} field that can
     * be obtained using the {@link TbShippingOrderMain#getUpdatedAt()} method.
     */
    final ComparableField<TbShippingOrderMain, Timestamp, Timestamp> UPDATED_AT = ComparableField.create(
        Identifier.UPDATED_AT,
        TbShippingOrderMain::getUpdatedAt,
        TbShippingOrderMain::setUpdatedAt,
        TypeMapper.identity(), 
        false
    );
    /**
     * This Field corresponds to the {@link TbShippingOrderMain} field that can
     * be obtained using the {@link TbShippingOrderMain#getCreatedAt()} method.
     */
    final ComparableField<TbShippingOrderMain, Timestamp, Timestamp> CREATED_AT = ComparableField.create(
        Identifier.CREATED_AT,
        TbShippingOrderMain::getCreatedAt,
        TbShippingOrderMain::setCreatedAt,
        TypeMapper.identity(), 
        false
    );
    
    /**
     * Returns the id of this TbShippingOrderMain. The id field corresponds to
     * the database column apollo_query.apollo_query.tb_shipping_order_main.id.
     * 
     * @return the id of this TbShippingOrderMain
     */
    long getId();
    
    /**
     * Returns the trackingId of this TbShippingOrderMain. The trackingId field
     * corresponds to the database column
     * apollo_query.apollo_query.tb_shipping_order_main.tracking_id.
     * 
     * @return the trackingId of this TbShippingOrderMain
     */
    long getTrackingId();
    
    /**
     * Returns the sourceId of this TbShippingOrderMain. The sourceId field
     * corresponds to the database column
     * apollo_query.apollo_query.tb_shipping_order_main.source_id.
     * 
     * @return the sourceId of this TbShippingOrderMain
     */
    int getSourceId();
    
    /**
     * Returns the platformTrackingId of this TbShippingOrderMain. The
     * platformTrackingId field corresponds to the database column
     * apollo_query.apollo_query.tb_shipping_order_main.platform_tracking_id.
     * 
     * @return the platformTrackingId of this TbShippingOrderMain
     */
    String getPlatformTrackingId();
    
    /**
     * Returns the retailerId of this TbShippingOrderMain. The retailerId field
     * corresponds to the database column
     * apollo_query.apollo_query.tb_shipping_order_main.retailer_id.
     * 
     * @return the retailerId of this TbShippingOrderMain
     */
    long getRetailerId();
    
    /**
     * Returns the shippingOption of this TbShippingOrderMain. The
     * shippingOption field corresponds to the database column
     * apollo_query.apollo_query.tb_shipping_order_main.shipping_option.
     * 
     * @return the shippingOption of this TbShippingOrderMain
     */
    byte getShippingOption();
    
    /**
     * Returns the gridId of this TbShippingOrderMain. The gridId field
     * corresponds to the database column
     * apollo_query.apollo_query.tb_shipping_order_main.grid_id.
     * 
     * @return the gridId of this TbShippingOrderMain
     */
    long getGridId();
    
    /**
     * Returns the carrierId of this TbShippingOrderMain. The carrierId field
     * corresponds to the database column
     * apollo_query.apollo_query.tb_shipping_order_main.carrier_id.
     * 
     * @return the carrierId of this TbShippingOrderMain
     */
    int getCarrierId();
    
    /**
     * Returns the agentId of this TbShippingOrderMain. The agentId field
     * corresponds to the database column
     * apollo_query.apollo_query.tb_shipping_order_main.agent_id.
     * 
     * @return the agentId of this TbShippingOrderMain
     */
    int getAgentId();
    
    /**
     * Returns the stationId of this TbShippingOrderMain. The stationId field
     * corresponds to the database column
     * apollo_query.apollo_query.tb_shipping_order_main.station_id.
     * 
     * @return the stationId of this TbShippingOrderMain
     */
    int getStationId();
    
    /**
     * Returns the carrierDriverId of this TbShippingOrderMain. The
     * carrierDriverId field corresponds to the database column
     * apollo_query.apollo_query.tb_shipping_order_main.carrier_driver_id.
     * 
     * @return the carrierDriverId of this TbShippingOrderMain
     */
    String getCarrierDriverId();
    
    /**
     * Returns the shippingState of this TbShippingOrderMain. The shippingState
     * field corresponds to the database column
     * apollo_query.apollo_query.tb_shipping_order_main.shipping_state.
     * 
     * @return the shippingState of this TbShippingOrderMain
     */
    byte getShippingState();
    
    /**
     * Returns the shippingReasonCode of this TbShippingOrderMain. The
     * shippingReasonCode field corresponds to the database column
     * apollo_query.apollo_query.tb_shipping_order_main.shipping_reason_code.
     * 
     * @return the shippingReasonCode of this TbShippingOrderMain
     */
    byte getShippingReasonCode();
    
    /**
     * Returns the remarkCode of this TbShippingOrderMain. The remarkCode field
     * corresponds to the database column
     * apollo_query.apollo_query.tb_shipping_order_main.remark_code.
     * 
     * @return the remarkCode of this TbShippingOrderMain
     */
    String getRemarkCode();
    
    /**
     * Returns the cityid of this TbShippingOrderMain. The cityid field
     * corresponds to the database column
     * apollo_query.apollo_query.tb_shipping_order_main.cityid.
     * 
     * @return the cityid of this TbShippingOrderMain
     */
    int getCityid();
    
    /**
     * Returns the acceptAt of this TbShippingOrderMain. The acceptAt field
     * corresponds to the database column
     * apollo_query.apollo_query.tb_shipping_order_main.accept_at.
     * 
     * @return the acceptAt of this TbShippingOrderMain
     */
    Timestamp getAcceptAt();
    
    /**
     * Returns the completeAt of this TbShippingOrderMain. The completeAt field
     * corresponds to the database column
     * apollo_query.apollo_query.tb_shipping_order_main.complete_at.
     * 
     * @return the completeAt of this TbShippingOrderMain
     */
    Timestamp getCompleteAt();
    
    /**
     * Returns the updatedAt of this TbShippingOrderMain. The updatedAt field
     * corresponds to the database column
     * apollo_query.apollo_query.tb_shipping_order_main.updated_at.
     * 
     * @return the updatedAt of this TbShippingOrderMain
     */
    Timestamp getUpdatedAt();
    
    /**
     * Returns the createdAt of this TbShippingOrderMain. The createdAt field
     * corresponds to the database column
     * apollo_query.apollo_query.tb_shipping_order_main.created_at.
     * 
     * @return the createdAt of this TbShippingOrderMain
     */
    Timestamp getCreatedAt();
    
    /**
     * Sets the id of this TbShippingOrderMain. The id field corresponds to the
     * database column apollo_query.apollo_query.tb_shipping_order_main.id.
     * 
     * @param id to set of this TbShippingOrderMain
     * @return   this TbShippingOrderMain instance
     */
    TbShippingOrderMain setId(long id);
    
    /**
     * Sets the trackingId of this TbShippingOrderMain. The trackingId field
     * corresponds to the database column
     * apollo_query.apollo_query.tb_shipping_order_main.tracking_id.
     * 
     * @param trackingId to set of this TbShippingOrderMain
     * @return           this TbShippingOrderMain instance
     */
    TbShippingOrderMain setTrackingId(long trackingId);
    
    /**
     * Sets the sourceId of this TbShippingOrderMain. The sourceId field
     * corresponds to the database column
     * apollo_query.apollo_query.tb_shipping_order_main.source_id.
     * 
     * @param sourceId to set of this TbShippingOrderMain
     * @return         this TbShippingOrderMain instance
     */
    TbShippingOrderMain setSourceId(int sourceId);
    
    /**
     * Sets the platformTrackingId of this TbShippingOrderMain. The
     * platformTrackingId field corresponds to the database column
     * apollo_query.apollo_query.tb_shipping_order_main.platform_tracking_id.
     * 
     * @param platformTrackingId to set of this TbShippingOrderMain
     * @return                   this TbShippingOrderMain instance
     */
    TbShippingOrderMain setPlatformTrackingId(String platformTrackingId);
    
    /**
     * Sets the retailerId of this TbShippingOrderMain. The retailerId field
     * corresponds to the database column
     * apollo_query.apollo_query.tb_shipping_order_main.retailer_id.
     * 
     * @param retailerId to set of this TbShippingOrderMain
     * @return           this TbShippingOrderMain instance
     */
    TbShippingOrderMain setRetailerId(long retailerId);
    
    /**
     * Sets the shippingOption of this TbShippingOrderMain. The shippingOption
     * field corresponds to the database column
     * apollo_query.apollo_query.tb_shipping_order_main.shipping_option.
     * 
     * @param shippingOption to set of this TbShippingOrderMain
     * @return               this TbShippingOrderMain instance
     */
    TbShippingOrderMain setShippingOption(byte shippingOption);
    
    /**
     * Sets the gridId of this TbShippingOrderMain. The gridId field corresponds
     * to the database column
     * apollo_query.apollo_query.tb_shipping_order_main.grid_id.
     * 
     * @param gridId to set of this TbShippingOrderMain
     * @return       this TbShippingOrderMain instance
     */
    TbShippingOrderMain setGridId(long gridId);
    
    /**
     * Sets the carrierId of this TbShippingOrderMain. The carrierId field
     * corresponds to the database column
     * apollo_query.apollo_query.tb_shipping_order_main.carrier_id.
     * 
     * @param carrierId to set of this TbShippingOrderMain
     * @return          this TbShippingOrderMain instance
     */
    TbShippingOrderMain setCarrierId(int carrierId);
    
    /**
     * Sets the agentId of this TbShippingOrderMain. The agentId field
     * corresponds to the database column
     * apollo_query.apollo_query.tb_shipping_order_main.agent_id.
     * 
     * @param agentId to set of this TbShippingOrderMain
     * @return        this TbShippingOrderMain instance
     */
    TbShippingOrderMain setAgentId(int agentId);
    
    /**
     * Sets the stationId of this TbShippingOrderMain. The stationId field
     * corresponds to the database column
     * apollo_query.apollo_query.tb_shipping_order_main.station_id.
     * 
     * @param stationId to set of this TbShippingOrderMain
     * @return          this TbShippingOrderMain instance
     */
    TbShippingOrderMain setStationId(int stationId);
    
    /**
     * Sets the carrierDriverId of this TbShippingOrderMain. The carrierDriverId
     * field corresponds to the database column
     * apollo_query.apollo_query.tb_shipping_order_main.carrier_driver_id.
     * 
     * @param carrierDriverId to set of this TbShippingOrderMain
     * @return                this TbShippingOrderMain instance
     */
    TbShippingOrderMain setCarrierDriverId(String carrierDriverId);
    
    /**
     * Sets the shippingState of this TbShippingOrderMain. The shippingState
     * field corresponds to the database column
     * apollo_query.apollo_query.tb_shipping_order_main.shipping_state.
     * 
     * @param shippingState to set of this TbShippingOrderMain
     * @return              this TbShippingOrderMain instance
     */
    TbShippingOrderMain setShippingState(byte shippingState);
    
    /**
     * Sets the shippingReasonCode of this TbShippingOrderMain. The
     * shippingReasonCode field corresponds to the database column
     * apollo_query.apollo_query.tb_shipping_order_main.shipping_reason_code.
     * 
     * @param shippingReasonCode to set of this TbShippingOrderMain
     * @return                   this TbShippingOrderMain instance
     */
    TbShippingOrderMain setShippingReasonCode(byte shippingReasonCode);
    
    /**
     * Sets the remarkCode of this TbShippingOrderMain. The remarkCode field
     * corresponds to the database column
     * apollo_query.apollo_query.tb_shipping_order_main.remark_code.
     * 
     * @param remarkCode to set of this TbShippingOrderMain
     * @return           this TbShippingOrderMain instance
     */
    TbShippingOrderMain setRemarkCode(String remarkCode);
    
    /**
     * Sets the cityid of this TbShippingOrderMain. The cityid field corresponds
     * to the database column
     * apollo_query.apollo_query.tb_shipping_order_main.cityid.
     * 
     * @param cityid to set of this TbShippingOrderMain
     * @return       this TbShippingOrderMain instance
     */
    TbShippingOrderMain setCityid(int cityid);
    
    /**
     * Sets the acceptAt of this TbShippingOrderMain. The acceptAt field
     * corresponds to the database column
     * apollo_query.apollo_query.tb_shipping_order_main.accept_at.
     * 
     * @param acceptAt to set of this TbShippingOrderMain
     * @return         this TbShippingOrderMain instance
     */
    TbShippingOrderMain setAcceptAt(Timestamp acceptAt);
    
    /**
     * Sets the completeAt of this TbShippingOrderMain. The completeAt field
     * corresponds to the database column
     * apollo_query.apollo_query.tb_shipping_order_main.complete_at.
     * 
     * @param completeAt to set of this TbShippingOrderMain
     * @return           this TbShippingOrderMain instance
     */
    TbShippingOrderMain setCompleteAt(Timestamp completeAt);
    
    /**
     * Sets the updatedAt of this TbShippingOrderMain. The updatedAt field
     * corresponds to the database column
     * apollo_query.apollo_query.tb_shipping_order_main.updated_at.
     * 
     * @param updatedAt to set of this TbShippingOrderMain
     * @return          this TbShippingOrderMain instance
     */
    TbShippingOrderMain setUpdatedAt(Timestamp updatedAt);
    
    /**
     * Sets the createdAt of this TbShippingOrderMain. The createdAt field
     * corresponds to the database column
     * apollo_query.apollo_query.tb_shipping_order_main.created_at.
     * 
     * @param createdAt to set of this TbShippingOrderMain
     * @return          this TbShippingOrderMain instance
     */
    TbShippingOrderMain setCreatedAt(Timestamp createdAt);
    
    enum Identifier implements ColumnIdentifier<TbShippingOrderMain> {
        
        ID ("id"),
        TRACKING_ID ("tracking_id"),
        SOURCE_ID ("source_id"),
        PLATFORM_TRACKING_ID ("platform_tracking_id"),
        RETAILER_ID ("retailer_id"),
        SHIPPING_OPTION ("shipping_option"),
        GRID_ID ("grid_id"),
        CARRIER_ID ("carrier_id"),
        AGENT_ID ("agent_id"),
        STATION_ID ("station_id"),
        CARRIER_DRIVER_ID ("carrier_driver_id"),
        SHIPPING_STATE ("shipping_state"),
        SHIPPING_REASON_CODE ("shipping_reason_code"),
        REMARK_CODE ("remark_code"),
        CITYID ("cityid"),
        ACCEPT_AT ("accept_at"),
        COMPLETE_AT ("complete_at"),
        UPDATED_AT ("updated_at"),
        CREATED_AT ("created_at");
        
        private final String columnName;
        private final TableIdentifier<TbShippingOrderMain> tableIdentifier;
        
        Identifier(String columnName) {
            this.columnName = columnName;
            this.tableIdentifier = TableIdentifier.of(getDbmsName(), getSchemaName(), getTableName());
        }
        
        @Override
        public String getDbmsName() {
            return "apollo_query";
        }
        
        @Override
        public String getSchemaName() {
            return "apollo_query";
        }
        
        @Override
        public String getTableName() {
            return "tb_shipping_order_main";
        }
        
        @Override
        public String getColumnName() {
            return this.columnName;
        }
        
        @Override
        public TableIdentifier<TbShippingOrderMain> asTableIdentifier() {
            return this.tableIdentifier;
        }
    }
}