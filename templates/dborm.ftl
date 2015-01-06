package ${doc.GatherRoot.entity.package};

<#--导入包-->
import javax.persistence.*;
import java.io.Serializable;
${NotNull?if_exists}
${DATE?if_exists}


@Entity
@Table(name = "${doc.GatherRoot.entity.table.@name}")
public class ${doc.GatherRoot.entity.@class} implements Serializable {
<#--映射相关的字段-->
<#list list as prop>
    ${prop.jpaField}
    @Column(name = "${prop.dataField}"<#if prop.dataType!="">, columnDefinition="${prop.dataType}"</#if>)
    private ${prop.javaType} ${prop.javaField};

</#list>
<#--生成相关的setter,getter方法-->
<#list list as prop>
    public ${prop.javaType} get${prop.javaField?cap_first}(){
        return ${prop.javaField};
    }

    public void set${prop.javaField?cap_first}(${prop.javaType} ${prop.javaField}){
        this.${prop.javaField} = ${prop.javaField};
    }

</#list>


}