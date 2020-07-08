package br.com.fooddelivery.api.controller;

import br.com.fooddelivery.api.dto.entry.PurchaseEntry;
import br.com.fooddelivery.api.dto.output.PurchaseOutput;
import br.com.fooddelivery.api.dto.output.PurchaseSummaryOutput;
import br.com.fooddelivery.api.mapper.PurchaseMapper;
import br.com.fooddelivery.api.mapper.PurchaseSummaryMapper;
import br.com.fooddelivery.domain.model.User;
import br.com.fooddelivery.domain.service.PurchaseOrderFlowService;
import br.com.fooddelivery.domain.service.PurchaseService;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/purchases")
public class PurchaseController {
    private final PurchaseService purchaseService;
    private final PurchaseMapper purchaseMapper;
    private final PurchaseSummaryMapper purchaseSummaryMapper;
    private final PurchaseOrderFlowService purchaseOrderFlowService;

    public PurchaseController(
            PurchaseService purchaseService,
            PurchaseMapper purchaseMapper,
            PurchaseSummaryMapper purchaseSummaryMapper,
            PurchaseOrderFlowService purchaseOrderFlowService
    ) {
        this.purchaseService = purchaseService;
        this.purchaseMapper = purchaseMapper;
        this.purchaseSummaryMapper = purchaseSummaryMapper;
        this.purchaseOrderFlowService = purchaseOrderFlowService;
    }

    @GetMapping
    public ResponseEntity<List<PurchaseSummaryOutput>> getPurchases() {
        List<PurchaseSummaryOutput> purchases = this.purchaseSummaryMapper.toCollectionOutput(this.purchaseService.getPurchases());

        CacheControl cache = CacheControl.maxAge(20, TimeUnit.SECONDS);

        return ResponseEntity.ok().cacheControl(cache).body(purchases);
    }

    /* example: Limiting the fields returned by the API with Jackson's @JsonFilter
    @GetMapping
    public ResponseEntity<MappingJacksonValue> getPurchases(@RequestParam(required = false) String fields) {
        var purchases = this.purchaseService.getPurchases();
        List<PurchaseSummaryOutput> purchaseSummaryOutput = this.purchaseSummaryMapper.toCollectionOutput(purchases);

        MappingJacksonValue purchasesWrapper = new MappingJacksonValue(purchaseSummaryOutput);
        SimpleFilterProvider filterProvider = new SimpleFilterProvider();

        filterProvider.addFilter("purchaseFilter", SimpleBeanPropertyFilter.serializeAll());

        if (StringUtils.isNotBlank(fields)) {
            String[] array = fields.split(",");
            filterProvider.addFilter("purchaseFilter", SimpleBeanPropertyFilter.filterOutAllExcept(array));
        }

        purchasesWrapper.setFilters(filterProvider);

        CacheControl cache = CacheControl.maxAge(20, TimeUnit.SECONDS);

        return ResponseEntity.ok().cacheControl(cache).body(purchasesWrapper);
    } */

    @GetMapping("/{purchaseCode}")
    public ResponseEntity<PurchaseOutput> getPurchaseByPurchaseCode(@PathVariable UUID purchaseCode) {
        var purchase = this.purchaseService.getByPurchaseCode(purchaseCode);

        CacheControl cache = CacheControl.maxAge(20, TimeUnit.SECONDS);

        return ResponseEntity
                .ok()
                .cacheControl(cache)
                .body(this.purchaseMapper.toOutput(purchase));
    }

    @PostMapping
    public ResponseEntity<PurchaseOutput> savePurchase(@RequestBody @Valid PurchaseEntry purchaseEntry) {
        var purchase = this.purchaseMapper.toDomain(purchaseEntry);

        // TODO pegar usuário autenticado
        purchase.setClient(new User());
        purchase.getClient().setId(1);

        purchase = this.purchaseService.savePurchase(purchase);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(purchase.getId())
                .toUri();

        return ResponseEntity.created(uri).body(this.purchaseMapper.toOutput(purchase));
    }

    @PutMapping("/{purchaseCode}/confirmed")
    public ResponseEntity<?> confirmedPurchase(@PathVariable UUID purchaseCode) {
        this.purchaseOrderFlowService.confirmedPurchase(purchaseCode);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{purchaseCode}/delivered")
    public ResponseEntity<?> deliveredPurchase(@PathVariable UUID purchaseCode) {
        this.purchaseOrderFlowService.deliveredPurchase(purchaseCode);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{purchaseCode}/canceled")
    public ResponseEntity<?> canceledPurchase(@PathVariable UUID purchaseCode) {
        this.purchaseOrderFlowService.canceledPurchase(purchaseCode);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
