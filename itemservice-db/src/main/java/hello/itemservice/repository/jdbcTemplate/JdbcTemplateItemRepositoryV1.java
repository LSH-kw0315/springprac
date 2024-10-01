package hello.itemservice.repository.jdbcTemplate;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class JdbcTemplateItemRepositoryV1 implements ItemRepository {

    private final JdbcTemplate template;

    public JdbcTemplateItemRepositoryV1(DataSource dataSource){
        this.template=new JdbcTemplate(dataSource);
    }

    @Override
    public Item save(Item item) {
        String sql="insert into item(item_name,price,quantity) values (?, ?, ?)";
        KeyHolder keyHolder=new GeneratedKeyHolder(); //PK가 자동 생성되어 관리되므로 이것이 필요
        template.update(con->{
            //자동 증가 키
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setString(1,item.getItemName());
            ps.setInt(2,item.getPrice());
            ps.setInt(3,item.getQuantity());
            return ps;
        },keyHolder);

        long pk = keyHolder.getKey().longValue();
        item.setId(pk);
        return item;
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        String sql="update item set item_name=?, price=?, quantity=? where id=?";
        template.update(
                sql,
                updateParam.getItemName(),
                updateParam.getPrice(),
                updateParam.getQuantity(),
                itemId
        );
    }

    @Override
    public Optional<Item> findById(Long id) {
        String sql="select id, item_name, price, quantity from item where id =?";
        try {
            Item item = template.queryForObject(sql, itemRowMapper(), id);
            return Optional.of(item);
        }catch (EmptyResultDataAccessException e){
            return Optional.empty();
        }

    }

    private RowMapper<Item> itemRowMapper(){
        //resultSet에서 rowNum만큼 커서를 옮겨준다.
        return ((rs, rowNum)->{
            Item item =new Item();
            item.setId(rs.getLong("id"));
            item.setItemName(rs.getString("item_name"));
            item.setPrice(rs.getInt("price"));
            item.setQuantity(rs.getInt("quantity"));
            return item;
        });
    }

    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        String itemName=cond.getItemName();
        Integer maxPrice=cond.getMaxPrice();

        String sql="select id, item_name, price, quantity from item";

        //동적 쿼리

        //둘 중 하나라도 null이 아니면 조건부를 추가하기
        if (StringUtils.hasText(itemName) || maxPrice != null) {
            sql += " where";
        }

        boolean andFlag = false;
        List<Object> param = new ArrayList<>();

        //아이템 이름이 있을 때 like 조건 추가하기
        if (StringUtils.hasText(itemName)) {
            sql += " item_name like concat('%',?,'%')";
            param.add(itemName);
            //아이템 이름이 있다면 and가 추가될 수 있도록 하기
            andFlag = true;
        }

        //최대 값이 있을 때 추가하기
        if (maxPrice != null) {
            //아이템 이름 조건이 있으면 and를 붙이도록
            if (andFlag) {
                sql += " and";
            }
            sql += " price <= ?";
            param.add(maxPrice);
        }
        log.info("sql={}", sql);

        return template.query(sql,itemRowMapper(),param.toArray());

    }
}
