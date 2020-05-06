package me.weekbelt.runningflex;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses(packagesOf = App.class)
public class PackageDependencyTests {

    private static final String ACCOUNT = "..modules.account..";
    private static final String TAG = "..modules.tag..";
    private static final String ACCOUNT_TAG = "..modules.accountTag..";
    private static final String ZONE = "..modules.zone..";
    private static final String ACCOUNT_ZONE = "..modules.accountZone..";

    @ArchTest
    ArchRule accountPackageRule = classes().that().resideInAPackage(ACCOUNT)
            .should().accessClassesThat().resideInAnyPackage(ACCOUNT_TAG, ACCOUNT_ZONE, TAG, ZONE, ACCOUNT);
}
